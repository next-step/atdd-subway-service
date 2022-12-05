package nextstep.subway.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Age;

class MemberDiscountPolicy {
    private final LoginMember loginMember;

    private MemberDiscountPolicy(LoginMember loginMember) {
        this.loginMember = loginMember;
    }

    public static MemberDiscountPolicy from(LoginMember loginMember) {
        return new MemberDiscountPolicy(loginMember);
    }

    public Fare discountFare(Fare fare) {
        if (loginMember.isNonMember()) {
            return fare;
        }
        return MemberDiscount.discountFare(fare, Age.from(loginMember.getAge()));
    }
}
