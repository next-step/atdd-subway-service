package nextstep.subway.path.domain;

public class FareCalculator {

	private static final int BASE_FARE = 1250;
	private static final int SURCHARGE = 100;
	private static final int BASE_DISTANCE_MAX = 10;
	private static final int SECOND_DISTANCE_STANDARD = 5;
	private static final int SECOND_DISTANCE_MAX = 50;
	private static final int THIRD_DISTANCE_STANDARD = 8;

	private FareCalculator() {
	}

	public static int calculate(int distance) {
		if (distance <= BASE_DISTANCE_MAX) {
			return BASE_FARE;
		}
		if (distance <= SECOND_DISTANCE_MAX) {
			return BASE_FARE + calculateOverFare(SECOND_DISTANCE_STANDARD, distance - BASE_DISTANCE_MAX);
		}
		return BASE_FARE + calculateOverFare(THIRD_DISTANCE_STANDARD, distance - BASE_DISTANCE_MAX);
	}

	private static int calculateOverFare(int standardDistance, int distance) {
		return (int)((Math.ceil((distance - 1) / standardDistance) + 1) * SURCHARGE);
	}

}
