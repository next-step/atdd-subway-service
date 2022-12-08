package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import nextstep.subway.common.exception.ErrorEnum;

public enum ExtraFare {
    BASIC_FARE(0, 10, 0, 0, distance -> 0),
    FIRST_RANGE_FARE(10, 50, 5, 100, ExtraFare::getExtraFareOfFirstRange),
    SECOND_RANGE_FARE(50, Integer.MAX_VALUE, 8, 100, ExtraFare::getExtraFareOfSecondRange);

    private final int minRange;
    private final int maxRange;
    private final int overDistance;
    private final int overFare;
    private final UnaryOperator<Integer> calculator;

    ExtraFare(int minRange, int maxRange, int overDistance, int overFare,
              UnaryOperator<Integer> calculator) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.overDistance = overDistance;
        this.overFare = overFare;
        this.calculator = calculator;
    }

    public static int calculate(int distance) {
        final ExtraFare extraFare = Arrays.stream(values())
                .filter(element -> element.minRange < distance && element.maxRange >= distance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_EXISTS_DISTANCE_AND_FARE_POLICY.message()));

        return extraFare.calculator.apply(distance);
    }

    private static int getExtraFareOfFirstRange(int distance) {
        return calculateOverFare(distance - BASIC_FARE.maxRange,
                FIRST_RANGE_FARE.overDistance,
                FIRST_RANGE_FARE.overFare
        );
    }

    private static int getExtraFareOfSecondRange(int distance) {
        int beforeFare = getExtraFareOfFirstRange(FIRST_RANGE_FARE.maxRange);
        return beforeFare + calculateOverFare(
                distance - FIRST_RANGE_FARE.maxRange,
                SECOND_RANGE_FARE.overDistance,
                SECOND_RANGE_FARE.overFare
        );
    }

    private static int calculateOverFare(int distance, int overDistance, int overFare) {
        double v = Math.ceil((distance - 1) / overDistance) + 1;
        return (int) (v * overFare);
    }
}
