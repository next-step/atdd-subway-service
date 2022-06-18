package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public enum AgeType {
    CHILD(6, 12, value -> calculateDiscount(value, AgeTypeConstants.CHILD_RATE)),
    TEENAGER(13, 18, value -> calculateDiscount(value, AgeTypeConstants.TEENAGER_RATE)),
    NONE(18, Integer.MAX_VALUE, value -> 0);

    private final int startRange;
    private final int endRange;
    private final Function<Integer, Integer> expression;

    private static class AgeTypeConstants {
        private static final int DEFAULT_FARE = 350;
        private static final double CHILD_RATE = 0.5;
        private static final double TEENAGER_RATE = 0.2;
    }

    AgeType(int startRange, int endRange, Function<Integer, Integer> expression) {
        this.startRange = startRange;
        this.endRange = endRange;
        this.expression = expression;
    }

    public static AgeType findByAge(int age) {
        return Arrays.stream(AgeType.values())
                .filter(ageType -> ageType.isIncluded(age))
                .findAny()
                .orElse(NONE);
    }

    public int calculateDiscountFare(int fare) {
        return this.expression.apply(fare);
    }

    private boolean isIncluded(int age) {
        return IntStream.rangeClosed(this.startRange, this.endRange)
                .anyMatch(num -> num == age);
    }

    private static int calculateDiscount(int value, double rate) {
        return (int) ((value - AgeTypeConstants.DEFAULT_FARE) * rate);
    }
}
