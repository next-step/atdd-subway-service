package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareAgePolicyType {
    BABY_AGE(age -> 0 <= age &&  age < 6, 0, 1),
    CHILD_AGE(age -> 6 <= age &&  age < 13, 350, 0.5),
    TEENAGER_AGE(age -> 13 <= age &&  age < 19, 350, 0.2),
    ADULT_AGE(age -> 19 <= age, 0, 0);

    private final Predicate<Integer> condition;
    private final int discountAmount;
    private final double discountRate;

    FareAgePolicyType(Predicate<Integer> condition, int discountAmount, double discountRate) {
        this.condition = condition;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
    }

    public static FareAgePolicyType of(int age) {
        return Arrays.stream(values())
                .filter(value -> value.condition.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int discountFare(int fare) {
        return (int) ((fare - discountAmount) * (1 - discountRate));
    }
}
