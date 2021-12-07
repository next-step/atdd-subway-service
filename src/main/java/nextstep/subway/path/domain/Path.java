package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private final List<Station> stations;
	private final int distance;
	private final int fare;

	private Path(List<Station> stations, int distance, FarePolicy farePolicy) {
		this.stations = stations;
		this.distance = distance;
		this.fare = farePolicy.calculate(distance);
	}

	public static Path of(List<Station> stations, int distance, FarePolicy farePolicy) {
		return new Path(stations, distance, farePolicy);
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
