package nextstep.subway.path.domain;

public enum FareDistancePolicyType {
    BASIC_DISTANCE(0, 0),
    MIDDLE_DISTANCE(10, 5),
    LONG_DISTANCE(50, 8);

    private static int BASIC_FARE = 1250;
    private static int ADDITIONAL_FARE_PER_UNIT = 100;

    private final int minDistance;
    private final int unitDistance;

    FareDistancePolicyType(int minDistance, int unitDistance) {
        this.minDistance = minDistance;
        this.unitDistance = unitDistance;
    }

    public static FareDistancePolicyType of(int distance) {
        if (LONG_DISTANCE.minDistance < distance) {
            return LONG_DISTANCE;
        }
        if (MIDDLE_DISTANCE.minDistance < distance) {
            return MIDDLE_DISTANCE;
        }
        return BASIC_DISTANCE;
    }

    public int calculateFare(int distance) {
        return calculateBasicFare() + calculateAdditionalFare(distance - minDistance, unitDistance);
    }

    private int calculateBasicFare() {
        if (this == BASIC_DISTANCE || this == MIDDLE_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + calculateAdditionalFare(LONG_DISTANCE.minDistance - MIDDLE_DISTANCE.minDistance,
                MIDDLE_DISTANCE.unitDistance);
    }

    private int calculateAdditionalFare(int distance, int unitDistance) {
        if (unitDistance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unitDistance) + 1) * ADDITIONAL_FARE_PER_UNIT);
    }
}
