package nextstep.subway.path.domain;

public class SubwayFare {

	public static int calculateDistanceFare(int distance) {
		return DistanceOverFareRule.valueOfDistance(distance)
			.calculateFare(distance);
	}

	public static int calculateReducedFare(int fare, int age) {
		return AgeDiscountRule.valueOfAge(age)
			.calculateFare(fare);
	}
}
