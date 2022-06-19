package nextstep.subway.path.domain;

public class FeeDiscountPolicy {
    private static final int DEFAULT_DEDUCTION_FEE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEEN_DISCOUNT_RATE = 0.8;

    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final int MIN_TEEN_AGE = 13;
    private static final int MAX_TEEN_AGE = 18;

    private FeeDiscountPolicy() {
    }

    public static Fee discount(Fee fee, int age) {
        return calculateDiscountFee(fee, age);
    }

    private static Fee calculateDiscountFee(Fee fee, int age) {
        if (isChild(age)) {
            return discountChildFee(fee);
        }
        if (isTeen(age)) {
            return discountTeenFee(fee);
        }
        return fee;
    }

    private static Fee discountChildFee(Fee fee) {
        final int discountFee = (int) ((fee.getFee() - DEFAULT_DEDUCTION_FEE) * CHILD_DISCOUNT_RATE);
        return new Fee(discountFee);
    }

    private static Fee discountTeenFee(Fee fee) {
        final int discountFee = (int) ((fee.getFee() - DEFAULT_DEDUCTION_FEE) * TEEN_DISCOUNT_RATE);
        return new Fee(discountFee);
    }

    private static boolean isChild(int age) {
        return age >= MIN_CHILD_AGE && age <= MAX_CHILD_AGE;
    }

    private static boolean isTeen(int age) {
        return age >= MIN_TEEN_AGE && age <= MAX_TEEN_AGE;
    }

}
