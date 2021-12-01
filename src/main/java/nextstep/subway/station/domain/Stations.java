package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stations {
	private final List<Station> values;

	private Stations(List<Station> values) {
		this.values = values;
	}

	public static Stations of(List<Station> stations) {
		if (stations == null) {
			return empty();
		}

		return new Stations(stations);
	}

	public static Stations empty() {
		return new Stations(new ArrayList<>());
	}

	public boolean anyMatch(Station station) {
		return values.stream()
			.anyMatch(value -> value == station);
	}

	public boolean noneMatch(Station station) {
		return values.stream()
			.noneMatch(value -> value == station);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public List<Station> getValues() {
		return values;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Stations stations = (Stations)o;
		return values.equals(stations.values);
	}

	@Override
	public int hashCode() {
		return Objects.hash(values);
	}
}
