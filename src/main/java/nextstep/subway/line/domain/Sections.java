package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {

	private static final int MINIMUM = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	private Sections(List<Section> sections) {
		this.sections.addAll(sections);
	}

	public static Sections of() {
		return new Sections();
	}

	public static Sections of(List<Section> sections) {
		return new Sections(sections);
	}

	public void add(Section newSection) {
		this.sections.add(newSection);
	}

	public Stations getStations() {
		if (this.sections.isEmpty()) {
			return Stations.of();
		}
		Map<Station, Station> stations = this.sections.stream()
			.collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
		Station firstStation = findFirstStation(stations);
		return sortStations(firstStation, stations);
	}

	private Stations sortStations(Station firstStation, Map<Station, Station> stations) {
		List<Station> orderedStations = new ArrayList<>();
		orderedStations.add(firstStation);
		Station station = firstStation;
		while (stations.containsKey(station)) {
			Station downStation = stations.get(station);
			orderedStations.add(downStation);
			station = downStation;
		}
		return Stations.of(orderedStations);
	}

	private Station findFirstStation(Map<Station, Station> stations) {
		return stations.keySet().stream()
			.filter(upStation -> !stations.containsValue(upStation))
			.findFirst()
			.orElseThrow(() ->
				new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "첫 상행선 종점을 찾을 수 없습니다"));
	}

	public void addStation(Section newSection) {
		validateUpdateSections(newSection);
		Stations stations = this.getStations();
		if (stations.isEmpty()) {
			this.sections.add(newSection);
			return;
		}
		updateUpStationIfExists(stations, newSection);
		updateDownStationIfExists(stations, newSection);
	}

	private void updateUpStationIfExists(Stations stations, Section newSection) {
		boolean isUpStationExisted = stations.isExists(newSection.getUpStation());
		if (!isUpStationExisted) {
			return;
		}
		this.sections.stream()
			.filter(it -> it.getUpStation() == newSection.getUpStation())
			.findFirst()
			.ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	private void updateDownStationIfExists(Stations stations, Section newSection) {
		boolean isDownStationExisted = stations.isExists(newSection.getDownStation());
		if (!isDownStationExisted) {
			return;
		}
		this.sections.stream()
			.filter(it -> it.getDownStation() == newSection.getDownStation())
			.findFirst()
			.ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	private void validateUpdateSections(Section newSection) {
		Stations stations = this.getStations();
		boolean isUpStationExisted = stations.isExists(newSection.getUpStation());
		boolean isDownStationExisted = stations.isExists(newSection.getDownStation());

		if (isUpStationExisted && isDownStationExisted) {
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "등록할 수 없는 구간 입니다.");
		}
	}

	public void removeStation(Station station, Line line) {
		validateRemoveStation();
		Optional<Section> backwardSection = findByUpStation(station);
		Optional<Section> forwardSection = findByDownStation(station);
		if (backwardSection.isPresent() && forwardSection.isPresent()) {
			Section section = Section.combine(forwardSection.get(), backwardSection.get());
			this.sections.add(section);
		}
		backwardSection.ifPresent(it -> line.getSections().remove(it));
		forwardSection.ifPresent(it -> line.getSections().remove(it));
	}

	private void remove(Section section) {
		this.sections.remove(section);
	}

	private void validateRemoveStation() {
		if (this.sections.size() <= MINIMUM) {
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "구간이 {}개 이하일 때는 삭제가 안됩니다", MINIMUM);
		}
	}

	private Optional<Section> findByUpStation(Station upStation) {
		return this.sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst();
	}

	private Optional<Section> findByDownStation(Station upStation) {
		return this.sections.stream()
			.filter(it -> it.getDownStation() == upStation)
			.findFirst();
	}

	public boolean containsAnySection(Stations stations) {
		return this.sections.stream()
			.anyMatch(section -> section.isContainsAny(stations));
	}

	public List<Section> toList() {
		return this.sections;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Sections sections1 = (Sections)o;

		return sections.equals(sections1.sections);
	}

	@Override
	public int hashCode() {
		return sections.hashCode();
	}

}
