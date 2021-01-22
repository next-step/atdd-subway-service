package nextstep.subway.path.domain;

public final class FareCalculator {
    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int MEDIUM_DISTANCE_POINT = 10;
    public static final int LONG_DISTANCE_POINT = 50;
    public static final int MEDIUM_DISTANCE_CHARGING_CRITERIA = 5;
    public static final int LONG_DISTANCE_CHARGING_CRITERIA = 8;
    public static final int BASIC_DISCOUNT_FARE = 350;
    public static final double DISCOUNT_RATE_FOR_CHILDREN = 0.5;
    public static final double DISCOUNT_RATE_FOR_YOUTH = 0.8;

    private FareCalculator() {
        throw new AssertionError();
    }

    public static int calculateDiscountedFareByAge(int fare, int age) {
        if (isChildren(age)) {
            return (int) ((fare - BASIC_DISCOUNT_FARE) * DISCOUNT_RATE_FOR_CHILDREN);
        }
        if (isYouth(age)) {
            return (int) ((fare - BASIC_DISCOUNT_FARE) * DISCOUNT_RATE_FOR_YOUTH);
        }
        return fare;
    }

    public static int calculateFareOf(int distance) {
        if (isMediumDistance(distance)) {
            return BASIC_FARE + calculateAdditionalFareForMediumDistance(distance);
        }
        if (isLongDistance(distance)) {
            return BASIC_FARE + calculateAdditionalFareForLongDistance(distance);
        }
        return BASIC_FARE;
    }

    private static boolean isYouth(int age) {
        return age >= 13 && age < 19;
    }

    private static boolean isChildren(int age) {
        return age >= 6 && age < 13;
    }

    private static boolean isMediumDistance(int distance) {
        return distance > MEDIUM_DISTANCE_POINT && distance <= LONG_DISTANCE_POINT;
    }

    private static boolean isLongDistance(int distance) {
        return distance > LONG_DISTANCE_POINT;
    }

    private static int calculateAdditionalFareForMediumDistance(int distance) {
        return calculateAdditionalFare(distance - MEDIUM_DISTANCE_POINT, MEDIUM_DISTANCE_CHARGING_CRITERIA);
    }

    private static int calculateAdditionalFareForLongDistance(int distance) {
        return calculateAdditionalFareForMediumDistance()
                + calculateAdditionalFare(distance - LONG_DISTANCE_POINT, LONG_DISTANCE_CHARGING_CRITERIA);
    }

    private static int calculateAdditionalFareForMediumDistance() {
        return calculateAdditionalFare(LONG_DISTANCE_POINT - MEDIUM_DISTANCE_POINT, MEDIUM_DISTANCE_CHARGING_CRITERIA);
    }

    private static int calculateAdditionalFare(int overDistance, int chargingCriteria) {
        return (int) ((Math.ceil((overDistance - 1) / chargingCriteria) + 1) * ADDITIONAL_FARE);
    }
}
