package nextstep.subway.path.domain;

import java.util.function.Function;

public enum DiscountAgeStrategy {

    ADULT(Math::toIntExact),
    TEENAGER(fee -> (int) ((fee - 350) * 0.8)),
    CHILD(fee -> (int) ((fee - 350) * 0.5));

    private final Function<Long, Integer> expression;

    DiscountAgeStrategy(Function<Long, Integer> expression) {
        this.expression = expression;
    }

    public static DiscountAgeStrategy getAgeType(int age) {

        if (isChildAge(age)){
            return CHILD;
        }

        if (isTeenagerAge(age)){
            return TEENAGER;
        }

        return ADULT;
    }

    public int calculate(long amount) {
        return expression.apply(amount);
    }

    private static boolean isChildAge(int age) {
        return age >= 6 && age < 12;
    }

    private static boolean isTeenagerAge(int age) {
        return age >= 13 && age < 19;
    }
}
