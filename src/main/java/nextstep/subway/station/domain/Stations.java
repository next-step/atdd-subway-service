package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stations {

	private List<Station> stations;

	private Stations(List<Station> stations) {
		this.stations = new ArrayList<>(stations);
	}

	public static Stations of(List<Station> stations) {
		return new Stations(stations);
	}

	public static Stations of() {
		return new Stations(new ArrayList<>());
	}

	public boolean containsAny(Station station1, Station station2) {
		Map<Station, Station> stationMap = this.toMap();
		if (stationMap.containsKey(station1) && stationMap.get(station1).equals(station2)) {
			return true;
		}
		return stationMap.containsKey(station2) && stationMap.get(station2).equals(station1);
	}

	private Map<Station, Station> toMap() {
		Map<Station, Station> stationMap = new HashMap<>();
		for (int i = 0; i < stations.size() - 1; i++) {
			stationMap.put(stations.get(i), stations.get(i + 1));
		}
		return stationMap;
	}

	public boolean isEmpty() {
		return this.stations.isEmpty();
	}

	public boolean isExists(Station station) {
		return stations.stream().anyMatch(it -> it.equals(station));
	}

	public boolean isEqualSize(int size) {
		return this.stations.size() == size;
	}

	public List<Station> toList() {
		return Collections.unmodifiableList(this.stations);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Stations stations1 = (Stations)o;

		return stations.equals(stations1.stations);
	}

	@Override
	public int hashCode() {
		return stations.hashCode();
	}

}
