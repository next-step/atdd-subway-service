package nextstep.subway.path.domain;

public class Fare {
	private int fare;

	public Fare(int distance, Lines lines, int age) {
		this.fare = AgeFarePolicy.find(age)
			.calculateDiscountedFare(DistanceFarePolicy.calculateFare(distance) + lines.calculateOverFare());
	}

	public int value() {
		return fare;
	}
}
