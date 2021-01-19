package nextstep.subway.path.domain;

public class SubwayFare {

	private static final int CHILDREN_AGE_CRITERIA = 13;
	private static final int TEENAGER_AGE_CRITERIA = 19;
	private static final int COMMON_DISCOUNT_FARE = 350;
	private static final float CHILDREN_DISCOUNT_FARE_RATE = 0.5f;
	private static final float TEENAGER_DISCOUNT_FARE_RATE = 0.8f;

	public static int calculateDistanceFare(int distance) {
		return DistanceFare.valueOfDistance(distance)
			.calculateFare(distance);
	}

	public static int calculateReducedFare(int fare, int age) {
		if (isTeenagers(age)) {
			return (int) ((fare - COMMON_DISCOUNT_FARE) * TEENAGER_DISCOUNT_FARE_RATE);
		}
		if (isChildren(age)) {
			return (int) ((fare - COMMON_DISCOUNT_FARE) * CHILDREN_DISCOUNT_FARE_RATE);
		}
		return fare;
	}

	private static boolean isTeenagers(int age) {
		return age < TEENAGER_AGE_CRITERIA && age >= CHILDREN_AGE_CRITERIA;
	}

	private static boolean isChildren(int age) {
		return age < CHILDREN_AGE_CRITERIA;
	}
}
