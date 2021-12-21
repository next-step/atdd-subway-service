package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class AgeFareCalculator {
    private static final int DEDUCT_FARE = 350;

    public static Fare calculateByAge(LoginMember loginMember, Fare fare) {
        if (loginMember.isTeenager()) {
            return fare.deduct(DEDUCT_FARE, DiscountPolicy.TEENAGER);
        }

        if (loginMember.isKid()) {
            return fare.deduct(DEDUCT_FARE, DiscountPolicy.KID);
        }

        return fare;
    }
}
