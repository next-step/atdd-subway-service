package nextstep.subway.fare.domain;

import java.util.function.IntFunction;

import static nextstep.subway.fare.domain.Fare.*;

public enum DistanceBasedExtraCharge {

    BASE(0, 10,
            distance -> ZERO_EXTRA_CHARGE),
    FIRST_INTERVAL(11, 50,
            distance -> (distance - BASE.endingPoint) / DISTANCE_FIRST_INTERVAL_DIVIDER * DISTANCE_EXTRA_CHARGE_UNIT),
    SECOND_INTERVAL(51, Integer.MAX_VALUE,
            distance -> (distance - FIRST_INTERVAL.endingPoint) / DISTANCE_SECOND_INTERVAL_DIVIDER * DISTANCE_EXTRA_CHARGE_UNIT +
                    calculate(FIRST_INTERVAL.endingPoint));

    private int startingPoint;
    private int endingPoint;
    private IntFunction<Integer> calculator;

    DistanceBasedExtraCharge(int startingPoint, int endingPoint, IntFunction<Integer> calculator) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.calculator = calculator;
    }

    public static int calculate(int distance) {
        if (distance >= BASE.startingPoint && distance <= BASE.endingPoint) {
            return BASE.calculator.apply(distance);
        }

        if (distance >= FIRST_INTERVAL.startingPoint && distance <= FIRST_INTERVAL.endingPoint) {
            return FIRST_INTERVAL.calculator.apply(distance);
        }
        return SECOND_INTERVAL.calculator.apply(distance);
    }
}
