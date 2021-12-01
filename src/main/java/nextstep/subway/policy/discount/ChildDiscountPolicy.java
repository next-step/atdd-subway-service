package nextstep.subway.policy.discount;

import nextstep.subway.policy.domain.Price;

public class ChildDiscountPolicy implements DiscountPolicy {
    private static final float DISCOUNT_RATIO = 0.5f;
    private static final int DEDUCTIBLE_FARE = 350;

    public ChildDiscountPolicy() {
    }

    @Override
    public Price apply(Price price) {
        return Price.of((int)((price.value() - DEDUCTIBLE_FARE) * (1 - DISCOUNT_RATIO)));
    }
}
