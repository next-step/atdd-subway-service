package nextstep.subway.fare.domain;


import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

import static nextstep.subway.fare.domain.Fare.*;

public enum AgeBasedDiscount {

    CHILD(age -> age >= 6 && age < 13, value -> (int) Math.round((value - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_CHILD_DISCOUNT_RATE))),
    TEENAGER(age -> age >= 13 && age < 19, value -> (int) Math.round((value - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_TEENAGER_DISCOUNT_RATE))),
    ADULT(age -> age >= 19, value -> value),
    TODDLER(age -> age < 6, value -> 0);

    private IntPredicate ageRange;
    private IntFunction<Integer> calculator;

    AgeBasedDiscount(IntPredicate ageRange, IntFunction<Integer> calculator) {
        this.ageRange = ageRange;
        this.calculator = calculator;
    }

    public static int calculate(int age, int totalFare) {
        return findAgeDiscount(age).calculator.apply(totalFare);
    }

    public static AgeBasedDiscount findAgeDiscount(int age) {
        return Arrays.stream(values())
                .filter(ageBasedDiscount -> ageBasedDiscount.isIncludedInRange(age))
                .findFirst()
                .orElse(null);
    }

    private boolean isIncludedInRange(int age) {
        return this.ageRange.test(age);
    }
}
