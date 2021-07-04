package nextstep.subway.path.domain;

import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountAgeStrategy {

    ADULT(DiscountAgeStrategy::isAdult, Math::toIntExact),
    TEENAGER(DiscountAgeStrategy::isTeenagerAge, DiscountAgeStrategy::calculateTeenager),
    CHILD(DiscountAgeStrategy::isChildAge, DiscountAgeStrategy::calculateChild);


    private static final int DEDUCTION_AMOUNT = 350;
    private final Predicate<Integer> ageCondition;
    private final Function<Long, Integer> expression;

    DiscountAgeStrategy(Predicate<Integer> ageCondition, Function<Long, Integer> expression) {
        this.ageCondition = ageCondition;
        this.expression = expression;
    }

    public static Integer getFee(int age, long fee) {
        for (DiscountAgeStrategy value : DiscountAgeStrategy.values()) {
            if (value.ageCondition.test(age)) {
                return value.expression.apply(fee);
            }
        }
        throw new IllegalArgumentException("age: " + age);
    }

    private static boolean isChildAge(int age) {
        return age >= 6 && age < 12;
    }

    private static boolean isTeenagerAge(int age) {
        return age >= 13 && age < 19;
    }

    private static boolean isAdult(int age) {
        return age >= 20;
    }

    private static int calculateTeenager(Long fee) {
        return (int) ((fee - DEDUCTION_AMOUNT) * 0.8);
    }

    private static int calculateChild(Long fee) {
        return (int) ((fee - DEDUCTION_AMOUNT) * 0.5);
    }
}
