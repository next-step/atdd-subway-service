package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.policy.DiscountPolicy;
import nextstep.subway.path.domain.policy.NonDiscountPolicy;

public class NonLoginMember implements LoginAbleMember {

    @Override
    public DiscountPolicy getDiscountPolicy() {
        return new NonDiscountPolicy();
    }
}
