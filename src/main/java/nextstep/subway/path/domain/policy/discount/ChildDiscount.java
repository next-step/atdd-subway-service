package nextstep.subway.path.domain.policy.discount;

public class ChildDiscount implements DiscountPolicy {

    private static final int DEDUCTION = 350;
    private static final double PRICE_RATE = 0.5;

    public ChildDiscount() {

    }

    @Override
    public double discount(int fare) {
        return (fare - DEDUCTION) * PRICE_RATE;
    }
}
