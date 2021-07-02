package nextstep.subway.path.additionalfarepolicy.memberfarepolicy;

import nextstep.subway.path.domain.Fare;

public class NoneDiscountPolicy implements MemberDiscountPolicy {
    @Override
    public Fare applyDiscount(Fare fare) {
        return fare;
    }
}
