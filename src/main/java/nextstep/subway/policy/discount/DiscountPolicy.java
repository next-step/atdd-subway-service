package nextstep.subway.policy.discount;

import nextstep.subway.policy.domain.Price;

public interface DiscountPolicy {
    public Price apply(Price price);
}
