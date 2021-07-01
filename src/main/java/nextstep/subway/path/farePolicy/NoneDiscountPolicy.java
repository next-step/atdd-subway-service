package nextstep.subway.path.farePolicy;

import nextstep.subway.path.domain.Fare;

public class NoneDiscountPolicy implements MemberDiscountPolicy {
    @Override
    public Fare applyDiscount(Fare fare) {
        return fare;
    }
}
