package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.AgeFareException;

import java.util.Arrays;
import java.util.function.IntFunction;

import static nextstep.subway.fare.exception.AgeFareExceptionCode.NONE_EXISTS_AGE;

public enum DistancePolicy {

    BASE(0, 10, distance -> distance),
    MIDDLE(10, 50, distance -> (int) ((Math.floor((distance - 1) / 5.0) + 1) * 100)),
    LONG(50, Integer.MAX_VALUE, distance -> (int) ((Math.floor((distance - 1) / 8.0) + 1 * 100)));

    private int minDistance;
    private int maxDistance;
    private IntFunction<Integer> policyFuction;

    DistancePolicy(int minDistance, int maxDistance, IntFunction<Integer> policyFuction) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.policyFuction = policyFuction;
    }

    public static DistancePolicy valueOfRange(int distance) {
        return Arrays.stream(DistancePolicy.values())
                .filter(it -> it.isRange(distance))
                .findAny()
                .orElseThrow(() -> new AgeFareException(NONE_EXISTS_AGE));
    }

    private boolean isRange(int distance) {
        return this.minDistance < distance && distance <= this.maxDistance;
    }

    public int getFare(int distance) {
        return this.policyFuction.apply(distance - this.minDistance);
    }
}

