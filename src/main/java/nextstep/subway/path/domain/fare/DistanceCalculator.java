package nextstep.subway.path.domain.fare;

public class DistanceCalculator {

	private static final int SHORT_DISTANCE = 10;
	private static final int MIDDLE_DISTANCE = 50;

	private static final int MIDDLE_INCREASE_FARE_DISTANCE = 5;
	private static final int LONG_INCREASE_FARE_DISTANCE = 8;

	private static final Money BASIC_FARE = Money.of(1250L);
	private static final int INCREASE_FARE_AMOUNT = 100;

	public static Money apply(int distance) {
		return calculateOverFare(distance);
	}

	private static Money calculateOverFare(int distance) {
		if (SHORT_DISTANCE >= distance) {
			return BASIC_FARE;
		}
		if (MIDDLE_DISTANCE >= distance) {
			distance -= SHORT_DISTANCE;
			int range = (distance - 1) / MIDDLE_INCREASE_FARE_DISTANCE;
			return calculateOverFare(SHORT_DISTANCE)
				.plus((int)((Math.ceil(range) + 1) * INCREASE_FARE_AMOUNT));
		}
		distance -= MIDDLE_DISTANCE;
		int range = (distance - 1) / LONG_INCREASE_FARE_DISTANCE;
		return calculateOverFare(MIDDLE_DISTANCE).plus((int)((Math.ceil(range) + 1) * INCREASE_FARE_AMOUNT));
	}
}
