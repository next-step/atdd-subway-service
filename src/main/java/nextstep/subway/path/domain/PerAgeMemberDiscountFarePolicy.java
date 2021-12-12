package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class PerAgeMemberDiscountFarePolicy implements MemberDiscountFarePolicy {
	@Override
	public int discount(int fare, LoginMember loginMember) {
		if (!loginMember.isLogin() || !FareDiscountAge.contains(loginMember.getAge())) {
			return fare;
		}

		FareDiscountAge fareDiscountAge = FareDiscountAge.findBy(loginMember.getAge());
		return fareDiscountAge.getDiscountFare(fare);
	}
}
