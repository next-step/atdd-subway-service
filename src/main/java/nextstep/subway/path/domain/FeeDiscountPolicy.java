package nextstep.subway.path.domain;

import java.util.Objects;

public class FeeDiscountPolicy {
    private static final int DEFAULT_DEDUCTION_FEE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEEN_DISCOUNT_RATE = 0.8;

    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final int MIN_TEEN_AGE = 13;
    private static final int MAX_TEEN_AGE = 18;

    private int fee;

    public FeeDiscountPolicy(int beforeDiscountFee, int age) {
        this.fee = calculateDiscountFee(beforeDiscountFee, age);
    }

    private int calculateDiscountFee(int fee, int age) {
        if (isChild(age)) {
            return discountChildFee(fee);
        }
        if (isTeen(age)) {
            return discountTeenFee(fee);
        }
        return fee;
    }

    private int discountChildFee(int fee) {
        return (int) ((fee - DEFAULT_DEDUCTION_FEE) * CHILD_DISCOUNT_RATE);
    }

    private int discountTeenFee(int fee) {
        return (int) ((fee - DEFAULT_DEDUCTION_FEE) * TEEN_DISCOUNT_RATE);
    }

    private boolean isChild(int age) {
        return age >= MIN_CHILD_AGE && age <= MAX_CHILD_AGE;
    }

    private boolean isTeen(int age) {
        return age >= MIN_TEEN_AGE && age <= MAX_TEEN_AGE;
    }

    public int getFee() {
        return fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeeDiscountPolicy that = (FeeDiscountPolicy) o;
        return fee == that.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fee);
    }
}
