package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {

	private int distance;
	private List<Station> stations;
	private PathPrice price;

	private Path(int distance, List<Station> stations, PathPrice price) {
		this.distance = distance;
		this.stations = stations;
		this.price = price;
	}

	public static Path getShortestPath(int distance, List<Station> stations) {
		return new Path(distance, stations, PathPrice.calculatePriceFromPath(distance));
	}

	public int getDistance() {
		return distance;
	}

	public List<Station> getStationsRoute() {
		return stations;
	}

	public int getPrice() {
		return price.getPrice();
	}
}
