package nextstep.subway.auth.domain;

public class TeenagerDiscountPolicy extends DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.2;

    private final Money discountAmount = new Money(350);

    @Override
    public Money getCharge() {
        return defaultCharge
                .minus(discountAmount)
                .divideByDecimalPoint(DISCOUNT_RATE);
    }
}
