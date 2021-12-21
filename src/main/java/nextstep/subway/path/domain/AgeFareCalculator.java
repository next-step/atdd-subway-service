package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class AgeFareCalculator {
    private static final int DEDUCT_FARE = 350;
    private static final int TEENAGER_DISCOUNT_RATE = 20;
    private static final int KID_DISCOUNT_RATE = 50;

    public static Fare calculateByAge(LoginMember loginMember, Fare fare) {
        if (loginMember.isTeenager()) {
            return fare.deduct(DEDUCT_FARE, TEENAGER_DISCOUNT_RATE);
        }

        if (loginMember.isKid()) {
            return fare.deduct(DEDUCT_FARE, KID_DISCOUNT_RATE);
        }

        return fare;
    }
}
