package nextstep.subway.path.domain.shortest;

import java.util.List;

import nextstep.subway.fare.domain.DistanceFare;
import nextstep.subway.station.domain.Station;

public class ShortestPath {

	private final List<Station> stations;
	private final double distance;

	private ShortestPath(List<Station> stations, double distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static ShortestPath of(List<Station> stations, double distance) {
		return new ShortestPath(stations, distance);
	}

	public List<Station> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}

	public int getFare() {
		return DistanceFare.calculate(distance).getFare();
	}
}
