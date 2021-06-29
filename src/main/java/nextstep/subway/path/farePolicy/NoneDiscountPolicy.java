package nextstep.subway.path.farePolicy;

import nextstep.subway.line.domain.Fare;

public class NoneDiscountPolicy implements MemberDiscountPolicyService{
    @Override
    public Fare applyDiscount(Fare fare) {
        return fare;
    }
}
