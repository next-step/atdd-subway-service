package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stations {
	private final List<Station> stations;

	public Stations() {
		this.stations = Collections.unmodifiableList(new ArrayList<>());
	}

	public Stations(List<Station> stations) {
		this.stations = Collections.unmodifiableList(stations);
	}

	public List<Station> getStations() {
		return stations;
	}

	public boolean isContains(Station station) {
		return stations.contains(station);
	}

	public boolean isNotContains(Station station) {
		return !isContains(station);
	}

	public boolean isEmpty() {
		return stations.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}
}
