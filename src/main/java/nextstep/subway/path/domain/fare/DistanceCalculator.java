package nextstep.subway.path.domain.fare;

public class DistanceCalculator {

	private static final int SHORT_DISTANCE = 10;
	private static final int MIDDLE_DISTANCE = 50;

	private static final int MIDDLE_INCREASE_FARE_DISTANCE = 5;
	private static final int LONG_INCREASE_FARE_DISTANCE = 8;

	public static final Money INCREASE_DISTANCE_FARE = Money.of(100);

	private static FareAge fareAge;

	public static Money apply(int distance, FareAge age) {
		fareAge = age;
		return calculateOverFare(distance);
	}

	private static Money calculateOverFare(int distance) {
		if (SHORT_DISTANCE >= distance) {
			return fareAge.getBasicFare();
		}
		if (MIDDLE_DISTANCE >= distance) {
			return calculateMiddleDistance(distance);
		}
		return calculateLongDistance(distance);
	}

	private static Money calculateLongDistance(int distance) {
		distance -= MIDDLE_DISTANCE;
		int range = (distance - 1) / LONG_INCREASE_FARE_DISTANCE;
		return calculateDistance(range, MIDDLE_DISTANCE);
	}

	private static Money calculateMiddleDistance(int distance) {
		distance -= SHORT_DISTANCE;
		int range = (distance - 1) / MIDDLE_INCREASE_FARE_DISTANCE;
		return calculateDistance(range, SHORT_DISTANCE);
	}

	private static Money calculateDistance(int range, int rangeDistance) {
		return calculateOverFare(rangeDistance)
			.plus((int)((Math.ceil(range) + 1) * fareAge.getDiscount() * INCREASE_DISTANCE_FARE.getMoney()));
	}
}
