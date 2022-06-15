package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.policy.DiscountPolicy;

public interface LoginAbleMember {
    DiscountPolicy getDiscountPolicy();
}
