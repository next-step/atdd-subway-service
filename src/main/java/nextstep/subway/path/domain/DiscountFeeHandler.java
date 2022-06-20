package nextstep.subway.path.domain;

public class DiscountFeeHandler extends FeeHandler {
    private static final int DEFAULT_DEDUCTION_FEE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEEN_DISCOUNT_RATE = 0.8;

    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final int MIN_TEEN_AGE = 13;
    private static final int MAX_TEEN_AGE = 18;

    private int age;

    public DiscountFeeHandler(FeeHandler feeHandler, int age) {
        super(feeHandler);
        this.age = age;
    }

    @Override
    public void calculate(Fee fee) {
        discountFee(fee);
        super.calculate(fee);
    }

    private void discountFee(Fee fee) {
        if (isChild(this.age)) {
            discountChildFee(fee);
        }
        if (isTeen(this.age)) {
            discountTeenFee(fee);
        }
    }

    private void discountChildFee(Fee fee) {
        final int discountFee = (int) ((fee.getFee() - DEFAULT_DEDUCTION_FEE) * CHILD_DISCOUNT_RATE);
        fee.update(discountFee);
    }

    private void discountTeenFee(Fee fee) {
        final int discountFee = (int) ((fee.getFee() - DEFAULT_DEDUCTION_FEE) * TEEN_DISCOUNT_RATE);
        fee.update(discountFee);
    }

    private boolean isChild(int age) {
        return age >= MIN_CHILD_AGE && age <= MAX_CHILD_AGE;
    }

    private boolean isTeen(int age) {
        return age >= MIN_TEEN_AGE && age <= MAX_TEEN_AGE;
    }
}
