package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.lang.Assert;

public class Stations {

	private final List<Station> stations;

	private Stations(List<Station> stations) {
		Assert.notNull(stations, "역 목록은 필수입니다.");
		this.stations = stations;
	}

	public static Stations from(List<Station> stations) {
		return new Stations(stations);
	}

	public static Stations empty() {
		return new Stations(Collections.emptyList());
	}

	public List<Station> list() {
		return Collections.unmodifiableList(stations);
	}

	public boolean notContains(Station upStation) {
		return !stations.contains(upStation);
	}

	public <R> List<R> mapToList(Function<Station, R> mapper) {
		return stations.stream()
			.map(mapper)
			.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Stations stations1 = (Stations)o;
		return Objects.equals(stations, stations1.stations);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(stations);
	}

	public Stations merge(Stations newStations) {
		List<Station> newList = new ArrayList<>(this.stations);
		newList.addAll(newStations.stations);
		return from(newList);
	}
}
