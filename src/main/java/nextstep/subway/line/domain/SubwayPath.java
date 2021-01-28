package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class SubwayPath {

	private List<Station> stations;
	private int distance;
	private Fare fare;

	public SubwayPath(final List<Station> stations, final int distance) {
		this(stations, distance, 0);
	}

	public SubwayPath(final List<Station> stations, final int distance, final int fare) {
		this(stations, distance, new Fare(fare));
	}

	private SubwayPath(final List<Station> stations, final int distance, final Fare fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
		this.addFare(DistanceFarePolicy.calculateDistanceFare(distance));
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare.getFare();
	}

	public void addFare(Fare fare) {
		this.fare = this.fare.add(fare);
	}

	public void calculateAgeFare(final int age) {
		this.fare = AgeFarePolicy.calculate(age, this.fare);
	}
}
