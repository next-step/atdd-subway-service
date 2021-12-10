package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

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

	public List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return new ArrayList<>();
		}
		Map<Station, Station> stations = this.sections.stream()
			.collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
		Station firstStation = findFirstStation(stations);
		return sortStations(firstStation, stations);
	}

	private List<Station> sortStations(Station firstStation, Map<Station, Station> stations) {
		List<Station> orderedStations = new ArrayList<>();
		orderedStations.add(firstStation);
		Station station = firstStation;
		while (stations.containsKey(station)) {
			Station downStation = stations.get(station);
			orderedStations.add(downStation);
			station = downStation;
		}
		return orderedStations;
	}

	private Station findFirstStation(Map<Station, Station> stations) {
		return stations.keySet().stream()
			.filter(upStation -> !stations.containsValue(upStation))
			.findFirst()
			.orElseThrow(() ->
				new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "첫 상행선 종점을 찾을 수 없습니다"));
	}

	public List<Section> getSections() {
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
