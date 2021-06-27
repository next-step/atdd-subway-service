package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private static final int DEFAULT_FARE = 1250;
	
	private List<Station> stations;
	private double distance;
	private int fare;

	protected Path() {
	}

	public Path(List<Station> stations, double distance) {
		this.stations = stations;
		this.distance = distance;
		this.fare = DEFAULT_FARE;
	}

	public List<Station> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
