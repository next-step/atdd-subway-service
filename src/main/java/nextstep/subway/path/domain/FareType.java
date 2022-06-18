package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public enum FareType {
    BASIC(0, 10, value -> FareTypeConstants.DEFAULT_FARE),
    MIDDLE(11, 50, value -> FareTypeConstants.DEFAULT_FARE + calculateFare(value, BASIC.endRange, 5)),
    ETC(51, 178, value -> FareTypeConstants.MIDDLE_FARE + calculateFare(value, MIDDLE.endRange, 8)),
    NONE(0, 0, value -> 0);

    private final int startRange;
    private final int endRange;
    private final Function<Integer, Integer> expression;

    private static class FareTypeConstants {
        private static final int DEFAULT_FARE = 1_250;
        private static final int MIDDLE_FARE = 2_050;
    }

    FareType(int startRange, int endRange, Function<Integer, Integer> expression) {
        this.startRange = startRange;
        this.endRange = endRange;
        this.expression = expression;
    }

    public static FareType findByDistance(int distance) {
        return Arrays.stream(FareType.values())
                .filter(fareType -> fareType.isIncluded(distance))
                .findAny()
                .orElse(NONE);
    }

    public int calculateByDistance(int distance) {
        return expression.apply(distance);
    }

    private boolean isIncluded(int distance) {
        return IntStream.rangeClosed(this.startRange, this.endRange)
                .anyMatch(num -> num == distance);
    }

    private static int calculateFare(Integer value, int endRange, int unit) {
        return (int) (Math.ceil(((value - endRange) - 1) / unit) + 1) * 100;
    }
}
