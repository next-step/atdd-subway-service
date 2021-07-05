package nextstep.subway.station.domain;

import java.util.Arrays;

public enum AgeDiscountPolicy {

    CHILD(6, 13, 0.2),
    YOUTH(13, 19, 0.5);

    private static final int DEDUCTION = 350;
    private static final int DISCOUNT_NONE = 0;

    private int minAge;
    private int maxAge;
    private double discountRate;

    AgeDiscountPolicy(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static int discountFare(int age, int fare) {
        return Arrays.stream(values())
                .filter(policy -> policy.isAgeMatch(age))
                .findFirst()
                .map(policy -> policy.discountFare(fare))
                .orElse(DISCOUNT_NONE);
    }

    private int discountFare(int fare) {
        return (int) ((fare - DEDUCTION) * discountRate);
    }

    private boolean isAgeMatch(int age) {
        return age >= minAge && age < maxAge;
    }
}
