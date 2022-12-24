package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum DiscountFarePolicy {
    OVER_19(19,999,0),
    OVER_13_UNDER_18(13, 18, 0.2),
    OVER_6_UNDER_12(6, 12, 0.5),
    UNDER_5(1,5,1);

    private final int minAge;
    private final int maxAge;
    private final double discountRate;
    private static final Fare DEDUCTION = Fare.from(350);

    DiscountFarePolicy(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static Fare calculate(Fare fare, int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.checkAgeRange(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("옳바른 나이의 범위가 아닙니다"))
                .discount(fare);
    }

    private boolean checkAgeRange(int age) {
        return this.minAge <= age && this.maxAge >= age;
    }

    private Fare discount(Fare fare) {
        if (this.discountRate == 0) {
            return fare;
        }
        return fare.minus(fare.minus(DEDUCTION).multiply(discountRate));
    }
}
