package nextstep.subway.path.domain;

public class Fare {
	private int fare;

	public Fare(int distance) {
		this.fare = DistanceFarePolicy.calculateFare(distance);
	}

	public int value() {
		return fare;
	}
}
