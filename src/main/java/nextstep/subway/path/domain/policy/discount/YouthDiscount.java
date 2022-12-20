package nextstep.subway.path.domain.policy.discount;

public class YouthDiscount implements DiscountPolicy {

    private static final int DEDUCTION = 350;
    private static final double PRICE_RATE = 0.8;

    public YouthDiscount() {

    }

    @Override
    public double discount(int fare) {
        return (fare - DEDUCTION) * PRICE_RATE;
    }
}
