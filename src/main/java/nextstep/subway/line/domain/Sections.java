package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
		CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public Sections(Section... sections) {
		for (Section section : sections) {
			this.sections.add(section);
		}
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}
		List<Station> stations = new ArrayList<>();
		Section firstSection = findFirstSection();
		addStationsInFirstSection(firstSection, stations);
		addAllStationsExceptFirstSection(firstSection, stations);
		return stations;
	}

	private void addStationsInFirstSection(Section firstSection, List<Station> stations) {
		stations.add(firstSection.getUpStation());
		stations.add(firstSection.getDownStation());
	}

	private void addAllStationsExceptFirstSection(Section currentSection, List<Station> stations) {
		while (isNextSection(currentSection)) {
			currentSection = getNextSection(currentSection);
			stations.add(currentSection.getDownStation());
		}
	}

	private Section getNextSection(Section currentSection) {
		return sections.stream()
			.filter(section -> section.equalsUpStation(currentSection.getDownStation()))
			.findFirst()
			.orElse(currentSection);
	}

	private boolean isNextSection(Section currentSection) {
		return sections.stream()
			.anyMatch(section -> section.equalsUpStation(currentSection.getDownStation()));
	}

	private Section findFirstSection() {
		Section firstSection = sections.get(0);
		while (!isFirstSection(firstSection)) {
			Section finalFistSection = firstSection;
			firstSection = sections.stream()
				.filter(section -> section.equalsDownStation(finalFistSection.getUpStation()))
				.findFirst()
				.orElse(finalFistSection);
		}
		return firstSection;
	}

	private boolean isFirstSection(Section firstSection) {
		return sections.stream()
			.noneMatch(section -> section.equalsDownStation(firstSection.getUpStation()));
	}

	public void addSection(Section section) {
		validateStationsExisted(section);
		validateStationNonMatch(section);

		if (getStations().isEmpty()) {
			sections.add(section);
			return;
		}

		if (isUpStationExisted(section)) {
			updateUpStation(section);
			sections.add(section);
			return;
		}

		if (isDownStationExisted(section)) {
			updateDownStation(section);
			sections.add(section);
			return;
		}
	}

	private void updateDownStation(Section addSection) {
		if (!isUpdatableDownStation(addSection)) {
			return;
		}
		sections.stream()
			.filter(section -> section.equalsDownStation(addSection.getDownStation()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("구간이 존재하지 않습니다."))
			.updateDownStation(addSection);
	}

	private boolean isUpdatableDownStation(Section addSection) {
		return sections.stream()
			.anyMatch(section -> section.equalsDownStation(addSection.getDownStation()));
	}

	private void updateUpStation(Section addSection) {
		if (!isUpdatableUpStation(addSection)) {
			return;
		}
		sections.stream()
			.filter(section -> section.equalsUpStation(addSection.getUpStation()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("구간이 존재하지 않습니다."))
			.updateUpStation(addSection);
	}

	private boolean isUpdatableUpStation(Section addSection) {
		return sections.stream()
			.anyMatch(section -> section.equalsUpStation(addSection.getUpStation()));
	}

	private boolean isDownStationExisted(Section section) {
		return getStations().stream()
			.anyMatch(station -> station.equals(section.getDownStation()));
	}

	private boolean isUpStationExisted(Section section) {
		return getStations().stream()
			.anyMatch(station -> station.equals(section.getUpStation()));
	}

	private void validateStationNonMatch(Section section) {
		if (getStations().stream()
			.noneMatch(station -> station.equals(section.getUpStation()) || station.equals(section.getDownStation()))) {
			throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
		}
	}

	private void validateStationsExisted(Section section) {
		if (getStations().containsAll(Arrays.asList(section.getUpStation(), section.getDownStation()))) {
			throw new IllegalArgumentException("이미 등록된 구간 입니다.");
		}
	}

	public void remove(Line line, Station station) {
		validateSectionsSize();
		List<Section> removeSections = findRemoveSections(station);
		if (isMiddleStation(removeSections)) {
			Station newUpStation = getNewUpStation(removeSections, station);
			Station newDownStation = getNewDownStation(removeSections, station);
			Distance newSectionDistance = getNewSectionDistance(removeSections);
			sections.add(new Section(line, newUpStation, newDownStation, newSectionDistance));
		}
		removeSections.stream()
			.forEach(section -> sections.remove(section));
	}

	private List<Section> findRemoveSections(Station station) {
		return sections.stream()
			.filter(section -> section.equalsDownStation(station) || section.equalsUpStation(station))
			.collect(Collectors.toList());
	}

	private Distance getNewSectionDistance(List<Section> sections) {
		return sections.stream()
			.map(Section::getDistance)
			.reduce((d1, d2) -> d1.increase(d2))
			.orElse(Distance.DUMMY_DISTANCE);
	}

	private Station getNewUpStation(List<Section> sections, Station station) {
		return sections.stream()
			.filter(section -> section.equalsDownStation(station))
			.findFirst()
			.orElse(Section.DUMMY_SECTION)
			.getUpStation();
	}

	private Station getNewDownStation(List<Section> sections, Station station) {
		return sections.stream()
			.filter(section -> section.equalsUpStation(station))
			.findFirst()
			.orElse(Section.DUMMY_SECTION)
			.getDownStation();
	}

	private boolean isMiddleStation(List<Section> sections) {
		return sections.size() == 2;
	}

	private void validateSectionsSize() {
		if (this.sections.size() <= 1) {
			throw new IllegalArgumentException("삭제 후 노선 내 구간은 1개 이상 존재해야 합니다.");
		}
	}
}
