package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AgeGroup;
import nextstep.subway.auth.domain.Member;

public class AgeFareCalculator {
    private static final int DEDUCT_FARE = 350;

    public static Fare calculateByAge(Member loginMember, Fare fare) {
        if (AgeGroup.TEENAGER.equals(loginMember.getAgeGroup())) {
            return fare.deduct(DEDUCT_FARE, DiscountPolicy.TEENAGER);
        }

        if (AgeGroup.KID.equals(loginMember.getAgeGroup())) {
            return fare.deduct(DEDUCT_FARE, DiscountPolicy.KID);
        }

        return fare;
    }
}
