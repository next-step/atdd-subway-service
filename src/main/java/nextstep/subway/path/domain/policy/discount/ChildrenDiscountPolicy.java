package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.discount.DiscountPolicyFactory.BASIC_DISCOUNT_STANDARD;

public class ChildrenDiscountPolicy implements FarePolicy {
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    @Override
    public int calculate(int fare) {
        return (int) (fare -  Math.ceil((fare - BASIC_DISCOUNT_STANDARD) * CHILDREN_DISCOUNT_RATE));
    }
}
