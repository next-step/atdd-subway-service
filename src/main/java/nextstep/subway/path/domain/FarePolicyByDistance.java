package nextstep.subway.path.domain;

public class FarePolicyByDistance implements FarePolicy {
	private static final int BASIC_FARE = 1250;

	@Override
	public int calculate(int distance) {
		if (distance <= 10) {
			return BASIC_FARE;
		}

		if (distance <= 50) {
			int firstOverFare = calculateOverFare(distance - 10, 5);
			return BASIC_FARE + firstOverFare;
		}

		int firstOverFare = calculateOverFare(40, 5);
		int secondOverFare = calculateOverFare(distance - 50, 8);
		return BASIC_FARE + firstOverFare + secondOverFare;
	}

	private int calculateOverFare(int overDistance, int distanceUnit) {
		return (int)((Math.ceil((overDistance - 1) / distanceUnit) + 1) * 100);
	}
}
