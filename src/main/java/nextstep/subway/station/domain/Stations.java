package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public boolean containsAnyStation(Stations other) {
		Set<Station> commonSet = new HashSet<>(this.stations);
		Set<Station> otherSet = new HashSet<>(other.stations);
		commonSet.retainAll(otherSet);
		return !commonSet.isEmpty();
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
