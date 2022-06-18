package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public interface DiscountPolicy {
    int calculate(int fare, LoginMember loginMember);
}
