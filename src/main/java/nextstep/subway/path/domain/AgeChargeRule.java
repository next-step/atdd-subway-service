package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeChargeRule {

    YOUTH(13, 18, 0.8d, 350),
    CHILD(6, 12, 0.5d, 350),
    DEFAULT(0, 0, 1.0d, 0);

    private int minAge;
    private int maxAge;
    private double discountRate;
    private int deduction;

    AgeChargeRule(int minAge, int maxAge, double discountRate, int deduction) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
        this.deduction = deduction;
    }

    public static AgeChargeRule valueOf(int age) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInclude(age))
                .findFirst()
                .orElse(DEFAULT);
    }

    public static long calculateFare(long fare, int age) {
        AgeChargeRule rule = AgeChargeRule.valueOf(age);
        return (long) ((fare - rule.deduction) * rule.discountRate);
    }

    private boolean isInclude(int age) {
        return this.minAge <= age && this.maxAge >= age;
    }
}
