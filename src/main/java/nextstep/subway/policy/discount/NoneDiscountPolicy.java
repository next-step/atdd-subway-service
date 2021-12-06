package nextstep.subway.policy.discount;

import nextstep.subway.policy.domain.Price;

public class NoneDiscountPolicy implements DiscountPolicy {
    public NoneDiscountPolicy() {
    }

    @Override
    public Price apply(Price price) {
        return price;
    }
}
