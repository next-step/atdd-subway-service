package nextstep.subway.policy.discount;

import nextstep.subway.policy.domain.Price;

public class YouthDiscountPolicy implements DiscountPolicy {
    private static final float DISCOUNT_RATIO = 0.2f;
    private static final int DEDUCTIBLE_FARE = 350;

    public YouthDiscountPolicy() {
    }

    @Override
    public Price apply(Price price) {
        return Price.of((int)((price.value() - DEDUCTIBLE_FARE) * (1 - DISCOUNT_RATIO)));
    }
}
