package nextstep.subway.fare.discount;

public class Teenager extends AgeDiscount {

    public static final int TEENAGER_DEDUCTION_FARE = 350;
    public static final double TEENAGER_DISCOUNT_RATIO = 0.8;

    @Override
    public int getDeductionFare() {
        return TEENAGER_DEDUCTION_FARE;
    }

    @Override
    public double getDiscountRate() {
        return TEENAGER_DISCOUNT_RATIO;
    }
}
