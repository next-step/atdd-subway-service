package nextstep.subway.path.domain;

public class SubwayFare {
	private static final int MINIMUM_FARE = 1250;
	private static final int OVER_FARE = 100;
	private static final long MEDIUM_DISTANCE_CRITERIA = 10;
	private static final long LONG_DISTANCE_CRITERIA = 50;
	private static final long MEDIUM_DISTANCE_CHARGING_CRITERIA = 5;
	private static final long LONG_DISTANCE_CHARGING_CRITERIA = 8;

	private static final int CHILDREN_AGE_CRITERIA = 13;
	private static final int TEENAGER_AGE_CRITERIA = 19;
	private static final int COMMON_DISCOUNT_FARE = 350;
	private static final float CHILDREN_DISCOUNT_FARE_RATE = 0.5f;
	private static final float TEENAGER_DISCOUNT_FARE_RATE = 0.8f;

	public static int calculateDistanceFare(int distance, int maxLineOverFare) {
		int baseFare = MINIMUM_FARE + maxLineOverFare;
		if (isMediumDistance(distance)) {
			return baseFare + calculateMediumDistanceOverFare(distance);
		}
		if (isLongDistance(distance)) {
			return baseFare + calculateLongDistanceFare(distance);
		}
		return baseFare;
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

	private static boolean isMediumDistance(int distance) {
		return distance > MEDIUM_DISTANCE_CRITERIA && distance <= LONG_DISTANCE_CRITERIA;
	}

	private static boolean isLongDistance(int distance) {
		return distance > LONG_DISTANCE_CRITERIA;
	}

	private static int calculateMediumDistanceOverFare(int distance) {
		return calculateAdditionalFare(distance - MEDIUM_DISTANCE_CRITERIA, MEDIUM_DISTANCE_CHARGING_CRITERIA);
	}

	private static int calculateLongDistanceFare(int distance) {
		return calculateMediumDistanceTotalOverFare()
			+ calculateAdditionalFare(distance - LONG_DISTANCE_CRITERIA, LONG_DISTANCE_CHARGING_CRITERIA);
	}

	private static int calculateMediumDistanceTotalOverFare() {
		return calculateAdditionalFare(LONG_DISTANCE_CRITERIA - MEDIUM_DISTANCE_CRITERIA, MEDIUM_DISTANCE_CHARGING_CRITERIA);
	}

	private static int calculateAdditionalFare(long overDistance, long chargingCriteria) {
		return (int) ((Math.ceil((overDistance - 1) / chargingCriteria) + 1) * OVER_FARE);
	}

	private static boolean isTeenagers(int age) {
		return age < TEENAGER_AGE_CRITERIA && age >= CHILDREN_AGE_CRITERIA;
	}

	private static boolean isChildren(int age) {
		return age < CHILDREN_AGE_CRITERIA;
	}
}
