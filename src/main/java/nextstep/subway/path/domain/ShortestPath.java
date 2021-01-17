package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class ShortestPath {
	private List<Station> stations;
	private int distance;
	private int fare;

	protected ShortestPath() {
	}

	public ShortestPath(List<Station> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
		fare = SubwayFare.calculateDistanceFare(distance);
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}

	public void addLineOverFare(int maxLineOverFare) {
		fare += maxLineOverFare;
	}

	public void calculateWithDiscount(int age) {
		fare = SubwayFare.calculateReducedFare(fare, age);
	}
}
