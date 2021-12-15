package nextstep.subway.path.domain;

public class ChildDiscountPolicy implements DiscountPolicy {
    public static final double CHILD_DISCOUNT_RATE = 0.5;
    public static final int DEDUCTION_FARE = 350;

    public ChildDiscountPolicy() {
    }

    @Override
    public int apply(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * CHILD_DISCOUNT_RATE);
    }
}
