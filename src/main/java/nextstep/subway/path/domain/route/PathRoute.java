package nextstep.subway.path.domain.route;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathRoute {

	private int distance;
	private List<Station> stations;

	public PathRoute(int distance, List<Station> stations) {
		this.distance = distance;
		this.stations = stations;
	}

	public int getDistance() {
		return distance;
	}

	public List<Station> getStationsRoute() {
		return stations;
	}

}
