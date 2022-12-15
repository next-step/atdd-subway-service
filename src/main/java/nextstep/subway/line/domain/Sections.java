package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {

	private static final String INVALID_SECTION_ERROR_MESSAGE = "지하철 구간은 반드시 존재해야 합니다.";
	private static final String NOT_INCLUDE_UP_DOWN_STATION_ERROR_MESSAGE = "등록할 수 없는 구간입니다.";
	private static final String SAME_UP_DOWN_STATION_ERROR_MESSAGE = "이미 등록된 구간입니다.";
	private static final String INVALID_MINIMUM_SECTION_COUNT_MESSAGE = "구간이 하나인 노선에서는 제거할 수 없습니다.";
	private static final int MINIMUM_SECTION_COUNT = 1;
	private static final String SECTION_NOT_FOUND_ERROR_MESSAGE = "구간 정보가 존재하지 않습니다.";

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	private Sections(List<Section> sections) {
		Assert.notNull(sections, INVALID_SECTION_ERROR_MESSAGE);
		this.sections.addAll(sections);
	}

	public static Sections from(Section section) {
		Assert.notNull(section, INVALID_SECTION_ERROR_MESSAGE);
		return new Sections(Collections.singletonList(section));
	}

	public static Sections from(List<Section> sections) {
		return new Sections(sections);
	}

	public static Sections empty() {
		return new Sections(Collections.emptyList());
	}

	public List<Section> list() {
		return sections;
	}

	public void setLine(Line line) {
		this.sections.forEach(section -> section.updateLine(line));
	}

	public Station firstUpStation() {
		List<Station> stations = allUpStations();
		stations.removeIf(station -> allDownStations().contains(station));
		return stations.get(0);
	}

	public Station lastDownStation() {
		List<Station> stations = allDownStations();
		stations.removeIf(station -> allUpStations().contains(station));
		return stations.get(0);
	}

	private List<Station> allUpStations() {
		return this.sections.stream()
			.map(Section::getUpStation)
			.collect(Collectors.toCollection(LinkedList::new));
	}

	private List<Station> allDownStations() {
		return this.sections.stream()
			.map(Section::getDownStation)
			.collect(Collectors.toCollection(LinkedList::new));
	}

	public Stations sortedStations() {
		List<Station> stations = new LinkedList<>();
		stations.add(firstUpStation());

		for (int i = 0; i < sections.size(); i++) {
			Station nextStation = sectionByUpStation(stations.get(i)).getDownStation();
			stations.add(nextStation);
		}
		return Stations.from(stations);
	}

	private Section sectionByUpStation(Station station) {
		return this.sections.stream()
			.filter(section -> section.getUpStation().name().equals(station.name()))
			.findFirst()
			.orElseThrow(() -> new NotFoundException("Section not exist"));
	}

	public void connect(Section section, List<Section> sectionsToRearrange) {
		validateAddSection(section);
		SectionConnector connector = SectionConnector.of(section, sectionsToRearrange);
		connector.connect(this);
	}

	private void validateAddSection(Section section) {
		validateSameUpDownStation(section);
		validateNotIncludeUpDownStation(section);
	}

	private void validateNotIncludeUpDownStation(Section section) {
		if (notIncludeUpDownStation(section)) {
			throw new InvalidDataException(NOT_INCLUDE_UP_DOWN_STATION_ERROR_MESSAGE);
		}
	}

	private void validateSameUpDownStation(Section section) {
		if (isSameUpDownStation(section)) {
			throw new DuplicateDataException(SAME_UP_DOWN_STATION_ERROR_MESSAGE);
		}
	}

	private boolean isSameUpDownStation(Section newSection) {
		return sections.stream()
			.filter(section -> section.isSameUpStation(newSection))
			.anyMatch(section -> section.isSameDownStation(newSection));
	}

	private boolean notIncludeUpDownStation(Section section) {
		return notIncludeDownStation(section) && notIncludeUpStation(section);
	}

	private boolean notIncludeDownStation(Section section) {
		return sortedStations().notContains(section.getUpStation());
	}

	private boolean notIncludeUpStation(Section section) {
		return sortedStations().notContains(section.getDownStation());
	}

	public void add(Section newSection) {
		sections.add(newSection);
	}

	public void remove(Section sectionByUpStation, Section sectionByDownStation) {
		validateRemoveSection(sectionByUpStation, sectionByDownStation);
		removeSection(sectionByUpStation, sectionByDownStation);
	}

	private void validateRemoveSection(Section sectionByUpStation, Section sectionByDownStation) {
		if (invalidSectionMinimumSize()) {
			throw new InvalidDataException(INVALID_MINIMUM_SECTION_COUNT_MESSAGE);
		}
		if (sectionByUpStation == null && sectionByDownStation == null) {
			throw new NotFoundException(SECTION_NOT_FOUND_ERROR_MESSAGE);
		}
	}

	private boolean invalidSectionMinimumSize() {
		return sections.size() <= MINIMUM_SECTION_COUNT;
	}

	private void removeSection(Section sectionByUpStation, Section sectionByDownStation) {
		SectionRemover.remove(this, sectionByUpStation, sectionByDownStation);
	}

	public void removeSection(Section section) {
		sections.remove(section);
	}

	public List<Section> getSections() {
		return this.sections;
	}

	public Sections merge(List<Section> sections) {
		List<Section> mergedSections = new ArrayList<>(this.sections);
		mergedSections.addAll(sections);
		return from(mergedSections);
	}

	public Fare maxExtraFare() {
		return sections.stream()
			.map(Section::extraFare)
			.max(Fare::compareTo)
			.orElse(Fare.zero());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Sections sections1 = (Sections)o;
		return Objects.equals(sections, sections1.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sections);
	}

}
