package nextstep.subway.fare.domain;


import nextstep.subway.exception.NotFoundException;

import java.util.Arrays;
import java.util.function.IntFunction;

import static nextstep.subway.utils.Constant.*;
import static nextstep.subway.utils.Message.OVER_FARE_POLICY_NOT_EXIST;


public enum OverDistancePolicy {
    TO_TEN(MIN_DISTANCE_OF_BASIC_FARE_SECTION,
            OverDistancePolicy::isValidForToTen,
            overDistance -> NO_OVER_FARE),
    TO_FIFTY(MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION,
            OverDistancePolicy::isValidForToFifty,
            overDistance -> (int) ((Math.floor((overDistance - 1) / PER_5_KM) + 1) * OVER_FARE_BY_DISTANCE)),
    OVER_FIFTY(MIN_DISTANCE_OF_SECOND_OVER_FARE_SECTION,
            OverDistancePolicy::isValidForOverFifty,
            overDistance -> (int) ((Math.floor((overDistance - 1) / PER_8_KM) + 1) * OVER_FARE_BY_DISTANCE) + TOTAL_OVER_FARE_IN_FIRST_OVER_FARE_SECTION);

    private int minDistance;
    private IntFunction<Boolean> valid;
    private IntFunction<Integer> policy;

    OverDistancePolicy(int minDistance, IntFunction<Boolean> valid, IntFunction<Integer> policy) {
        this.minDistance = minDistance;
        this.valid = valid;
        this.policy = policy;
    }

    public static OverDistancePolicy findPolicyByDistance(int distance) {
        return Arrays.stream(OverDistancePolicy.values())
                .filter(it -> it.isFareSectionIncluding(distance))
                .findAny()
                .orElseThrow(() -> new NotFoundException(OVER_FARE_POLICY_NOT_EXIST));
    }

    public int calculateOverFare(int distance) {
        return this.policy.apply(distance - this.minDistance);
    }

    private boolean isFareSectionIncluding(int distance) {
        return this.valid.apply(distance);
    }

    private static boolean isValidForToTen(int distance) {
        return MIN_DISTANCE_OF_BASIC_FARE_SECTION <= distance && distance <= MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION;
    }

    private static boolean isValidForToFifty(int distance) {
        return MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION < distance && distance <= MIN_DISTANCE_OF_SECOND_OVER_FARE_SECTION;
    }

    private static boolean isValidForOverFifty(int distance) {
        return distance > MIN_DISTANCE_OF_SECOND_OVER_FARE_SECTION;
    }
}
