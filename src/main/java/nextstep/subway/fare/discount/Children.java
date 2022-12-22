package nextstep.subway.fare.discount;

public class Children extends AgeDiscount {

    public static final int CHILDREN_DEDUCTION_FARE = 350;
    public static final double CHILDREN_DISCOUNT_RATIO = 0.5;

    @Override
    public int getDeductionFare() {
        return CHILDREN_DEDUCTION_FARE;
    }

    @Override
    public double getDiscountRate() {
        return CHILDREN_DISCOUNT_RATIO;
    }
}
