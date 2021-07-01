package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DefaultDiscountPolicy implements FarePolicy {
    @Override
    public int calculate(int fare) {
        return fare;
    }
}
