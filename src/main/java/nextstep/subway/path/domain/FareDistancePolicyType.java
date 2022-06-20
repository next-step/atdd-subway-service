package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareDistancePolicyType {
    BASIC_DISTANCE(distance -> 0 < distance &&  distance <= 10, 0, 0),
    MIDDLE_DISTANCE(distance -> 10 < distance && distance <= 50, 10, 5),
    LONG_DISTANCE(distance -> 50 < distance, 50, 8);

    private final static int BASIC_FARE = 1250;
    private final static int ADDITIONAL_FARE_PER_UNIT = 100;

    private final Predicate<Integer> condition;
    private final int minDistance;
    private final int unitDistance;

    FareDistancePolicyType(Predicate<Integer> condition, int minDistance, int unitDistance) {
        this.condition = condition;
        this.minDistance = minDistance;
        this.unitDistance = unitDistance;
    }

    public static FareDistancePolicyType of(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.condition.test(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int calculateFare(int distance) {
        return calculateBasicFare(distance) + calculateAdditionalFare(distance - minDistance, unitDistance);
    }

    private int calculateBasicFare(int distance) {
        if (BASIC_DISTANCE.condition.test(distance) || MIDDLE_DISTANCE.condition.test(distance)) {
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
