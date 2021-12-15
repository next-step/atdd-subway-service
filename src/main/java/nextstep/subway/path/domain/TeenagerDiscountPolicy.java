package nextstep.subway.path.domain;

public class TeenagerDiscountPolicy implements DiscountPolicy {
    public static final double TEENAGER_DISCOUNT_RATE = 0.8;
    public static final int DEDUCTION_FARE = 350;

    public TeenagerDiscountPolicy() {
    }

    @Override
    public int apply(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATE);
    }
}
