package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private List<Station> stations;
	private double distance;

	protected Path() {
	}

	public Path(List<Station> stations, double distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<Station> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}
}
