package nextstep.subway.path.additionalfarepolicy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.additionalfarepolicy.distanceparepolicy.DistanceOverFarePolicy;
import nextstep.subway.path.additionalfarepolicy.memberfarepolicy.MemberDiscountPolicy;
import nextstep.subway.path.domain.Fare;

public class AdditionalFareCalculator {
    private AdditionalFareCalculator() {}

    public static Fare calculate(Fare lineFare, Distance distance, LoginMember loginMember) {
        return MemberDiscountPolicy.getPolicy(loginMember).applyDiscount(lineFare.sum(DistanceOverFarePolicy.calculate(distance)));
    }
}
