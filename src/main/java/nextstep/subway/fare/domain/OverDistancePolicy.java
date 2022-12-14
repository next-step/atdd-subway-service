package nextstep.subway.fare.domain;


import nextstep.subway.exception.NotFoundException;

import java.util.Arrays;
import java.util.function.IntFunction;

import static nextstep.subway.utils.Constant.*;
import static nextstep.subway.utils.Message.OVER_FARE_POLICY_NOT_EXIST;


public enum OverDistancePolicy {
    TO_TEN(0, 10,
            overDistance -> NO_OVER_FARE),
    TO_FIFTY(10, 50,
            overDistance -> (int) ((Math.floor((overDistance - 1) / PER_5_KM) + 1) * OVER_FARE_BY_DISTANCE)),
    OVER_FIFTY(50, Integer.MAX_VALUE,
            overDistance -> (int) ((Math.floor((overDistance - 1) / PER_8_KM) + 1) * OVER_FARE_BY_DISTANCE) + TOTAL_OVER_FARE_IN_FIRST_OVER_FARE_SECTION);

    private int minDistance;
    private int maxDistance;
    private IntFunction<Integer> policy;

    OverDistancePolicy(int minDistance, int maxDistance, IntFunction<Integer> policy) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.policy = policy;
    }

    public static OverDistancePolicy findPolicyByDistance(int distance) {
        return Arrays.stream(OverDistancePolicy.values())
                .filter(it -> it.ranged(distance))
                .findAny()
                .orElseThrow(() -> new NotFoundException(OVER_FARE_POLICY_NOT_EXIST));
    }

    private boolean ranged(int distance) {
        return this.minDistance <= distance && distance < this.maxDistance;
    }

    public int calculateOverFare(int distance) {
        return this.policy.apply(distance - this.minDistance);
    }
}
