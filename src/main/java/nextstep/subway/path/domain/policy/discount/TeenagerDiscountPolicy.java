package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.discount.DiscountPolicyFactory.BASIC_DISCOUNT_STANDARD;

public class TeenagerDiscountPolicy implements FarePolicy {
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;

    @Override
    public int calculate(int fare) {
        return (int) (fare - Math.ceil((fare - BASIC_DISCOUNT_STANDARD) * TEENAGER_DISCOUNT_RATE));
    }
}
