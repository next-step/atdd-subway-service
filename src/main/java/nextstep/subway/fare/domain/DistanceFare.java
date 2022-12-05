package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.AgeFareException;

import java.util.Arrays;
import java.util.function.IntFunction;

import static nextstep.subway.fare.exception.AgeFareExceptionCode.NONE_EXISTS_AGE;

public enum DistanceFare {

    BASE(0, 10),
    MIDDLE(10, 50),
    LONG(50, Integer.MAX_VALUE);

    private int minDistance;
    private int maxDistance;
    private IntFunction<Integer> policyFuction;

    DistanceFare(int minDistance, int maxDistance, IntFunction<Integer> policyFuction) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.policyFuction = policyFuction;
    }

    public static DistanceFare valueOfRange(int age) {
        return Arrays.stream(DistanceFare.values())
                .filter(it -> it.isRange(age))
                .findAny()
                .orElseThrow(() -> new AgeFareException(NONE_EXISTS_AGE));
    }

    private boolean isRange(int distance) {
        return this.minDistance < distance && distance <= this.maxDistance;
    }

    //거리입력하면, 얼마나오는지

}
