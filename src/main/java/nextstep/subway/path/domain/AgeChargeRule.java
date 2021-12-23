package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeChargeRule {
    어린이(6, 13, 350, 0.5),
    청소년(13, 19, 350, 0.8),
    DEFAULT(0, 0, 0, 1);

    private int minAge;
    private int maxAge;
    private int deductionCharge;
    private double discountRate;

    AgeChargeRule(int minAge, int maxAge, int deductionCharge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionCharge = deductionCharge;
        this.discountRate = discountRate;
    }

    public static int calculateChargeByAge(int fare, int age) {
        AgeChargeRule rule = valueOf(age);
        if(rule.equals(어린이)) {
            return calculationFormula(fare, rule);
        }
        if(rule.equals(청소년)) {
            return calculationFormula(fare, rule);
        }
        return fare;
    }

    private static int calculationFormula(int fare, AgeChargeRule rule) {
        return (int) ((fare - rule.deductionCharge) * rule.discountRate);
    }

    private static AgeChargeRule valueOf(int age) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInclude(age))
                .findFirst()
                .orElse(DEFAULT);
    }

    private boolean isInclude(int age) {
        return this.minAge <= age && this.maxAge > age;
    }
}
