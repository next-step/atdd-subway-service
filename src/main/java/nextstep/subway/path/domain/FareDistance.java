package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntFunction;

public enum FareDistance {
    BASIC(0, 10, distance -> Constant.BASIC_PRICE),
    MIDDLE(11, 50, distance ->
            BASIC.expression.apply(distance) + calculateFare(distance - BASIC.end, 5)),
    LONG(51, 178, distance ->
            MIDDLE.expression.apply(MIDDLE.end) + calculateFare(distance - MIDDLE.end, 8)),
    NOT_MATCH(0, 0, distance -> 0);

    private static final int ZERO = 0;
    private static final int INCREMENT_FARE = 100;

    private static class Constant {
        private static final int BASIC_PRICE = 1_250;
    }

    private final int start;
    private final int end;
    private final IntFunction<Integer> expression;

    FareDistance(int start, int end, IntFunction<Integer> expression) {
        this.start = start;
        this.end = end;
        this.expression = expression;
    }

    public static int calculate(int distance) {
        FareDistance fareDistance = findByDistance(distance);
        return fareDistance.expression.apply(distance);
    }

    private static FareDistance findByDistance(int distance) {
        return Arrays.stream(FareDistance.values())
                .filter(fareDistance -> fareDistance.isBetween(distance))
                .findFirst()
                .orElse(NOT_MATCH);
    }

    private boolean isBetween(int distance) {
        return start <= distance && distance <= end;
    }

    private static int calculateFare(int distance, int condition) {
        if (distance <= ZERO) {
            return ZERO;
        }

        return (int) ((Math.ceil((distance - 1) / condition) + 1) * INCREMENT_FARE);
    }
}
