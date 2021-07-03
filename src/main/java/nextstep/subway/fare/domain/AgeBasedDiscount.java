package nextstep.subway.fare.domain;

import java.util.function.IntFunction;

import static nextstep.subway.fare.domain.Fare.*;

public enum AgeBasedDiscount {

    CHILD(6, 13, value -> (int) Math.round((value - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_CHILD_DISCOUNT_RATE))),
    TEENAGER(13, 19, value -> (int) Math.round((value - AGE_DISCOUNT_DEDUCTION_FARE) * (1 - AGE_TEENAGER_DISCOUNT_RATE))),
    ADULT(20, 200, value -> value);

    private int startingPoint;
    private int endingPoint;
    private IntFunction<Integer> calculator;

    AgeBasedDiscount(int startingPoint, int endingPoint, IntFunction<Integer> calculator) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.calculator = calculator;
    }

    public static int calculate(int age, int totalFare) {
        if (age >= CHILD.startingPoint && age < CHILD.endingPoint) {
            return CHILD.calculator.apply(totalFare);
        }
        if (age >= TEENAGER.startingPoint && age < TEENAGER.endingPoint) {
            return TEENAGER.calculator.apply(totalFare);
        }
        return ADULT.calculator.apply(totalFare);
    }
}
