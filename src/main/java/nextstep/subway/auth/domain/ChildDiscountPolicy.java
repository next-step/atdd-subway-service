package nextstep.subway.auth.domain;

public class ChildDiscountPolicy extends DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.5;

    private final Money discountAmount = Money.from(350);

    @Override
    public Money getCharge() {
        return defaultCharge
                .minus(discountAmount)
                .divideByDecimalPoint(DISCOUNT_RATE);
    }
}