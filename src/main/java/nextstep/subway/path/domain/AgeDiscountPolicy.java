package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    TEENAGER(13, 18, 350, 0.2) {
        @Override
        public int calculateAgeDiscountCharge(int charge) {
            return calculateDiscountCharge(charge, this.discountCharge(), this.disCountPercent());
        }
    },
    CHILDREN(6, 12, 350, 0.5) {
        @Override
        public int calculateAgeDiscountCharge(int charge) {
            return calculateDiscountCharge(charge, this.discountCharge(), this.disCountPercent());
        }
    },
    NOT_APPLICABLE(0, 0, 0, 0) {
        @Override
        public int calculateAgeDiscountCharge(int charge) {
            return charge;
        }
    };

    private final int minAge;
    private final int maxAge;
    private final int discountCharge;
    private final double disCountPercent;

    AgeDiscountPolicy(int minAge, int maxAge, int discountCharge, double disCountPercent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountCharge = discountCharge;
        this.disCountPercent = disCountPercent;
    }

    public static AgeDiscountPolicy ageDiscountPolicy(Integer age) {
        if (age == null) {
            return NOT_APPLICABLE;
        }

        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(it -> it.findAgeDiscountPolicy(age))
                .findFirst()
                .orElse(NOT_APPLICABLE);
    }

    public abstract int calculateAgeDiscountCharge(int charge);

    private boolean findAgeDiscountPolicy(int age) {
        return age >= minAge && age <= maxAge;
    }

    private static int calculateDiscountCharge(int charge, int discountCharge, double disCountPercent) {
        return (int) ((charge - discountCharge) * (1.0 - disCountPercent));
    }

    public int discountCharge() {
        return discountCharge;
    }

    public double disCountPercent() {
        return disCountPercent;
    }
}
