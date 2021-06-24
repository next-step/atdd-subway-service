package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nextstep.subway.line.domain.Section;

public class SortedStations {
	private List<Station> stations = new ArrayList<>();

	public SortedStations() {
	}

	public boolean isContains(Station station) {
		return this.stations.contains(station);
	}

	public boolean containsAll(Section section) {
		return this.stations.containsAll(Arrays.asList(section.getUpStation(), section.getDownStation()));
	}

	public boolean isUnRelatedSection(Section section) {
		return !this.stations.isEmpty()
			&& !this.isContains(section.getUpStation())
			&& !this.isContains(section.getDownStation());

	}

	public void addStation(Station station) {
		this.stations.add(station);
	}

	public List<Station> getStations() {
		return stations;
	}

	public boolean isEmpty() {
		return this.stations.isEmpty();
	}
}
