package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public interface MemberDiscountFarePolicy {
	int discount(int fare, LoginMember loginMember);
}
