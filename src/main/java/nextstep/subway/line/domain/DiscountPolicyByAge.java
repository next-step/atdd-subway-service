package nextstep.subway.line.domain;

import java.util.Arrays;

public enum DiscountPolicyByAge {
    나이_19세_이상(19, 100, 0),
    나이_13세_이상_19세_미만(13, 18, 20),
    나이_6세_이상_13세_미만(6, 12, 50),
    나이_1세_이상_6세_미만(1, 5, 100),
    ;

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private final int minAge;
    private final int maxAge;
    private final double discountRate;

    DiscountPolicyByAge(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static Fare calculate(int fare, int age) {
        return Fare.from(Arrays.stream(values())
                .filter(policy -> policy.checkAgeRange(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .discount(fare));
    }

    private boolean checkAgeRange(int age) {
        return this.minAge <= age && this.maxAge >= age;
    }

    private int discount(int fare) {
        if (this.discountRate == 0) {
            return fare;
        }
        return fare - (int) ((fare - DEFAULT_DISCOUNT_FARE) * (this.discountRate / 100));
    }
}
