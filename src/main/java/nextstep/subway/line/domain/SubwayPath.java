package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class SubwayPath {

	private List<Station> stations;
	private int distance;

	public SubwayPath(final List<Station> stations, final int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

}
