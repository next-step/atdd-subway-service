package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class ShortestPath {
	private List<Station> stations;
	private long distance;

	protected ShortestPath() {
	}

	public ShortestPath(List<Station> stations, long distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}
}
