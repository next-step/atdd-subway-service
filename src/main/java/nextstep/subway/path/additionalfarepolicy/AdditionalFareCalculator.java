package nextstep.subway.path.additionalfarepolicy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.additionalfarepolicy.distanceparepolicy.OverFareByDistance;
import nextstep.subway.path.additionalfarepolicy.memberfarepolicy.MemberDiscountPolicy;
import nextstep.subway.path.domain.Fare;

public class AdditionalFareCalculator implements AdditionalFarePolicy {
    @Override
    public Fare calculate(Fare lineFare, Distance distance, MemberDiscountPolicy memberDiscountPolicy) {
        return memberDiscountPolicy.applyDiscount(lineFare.sum(OverFareByDistance.calculate(distance)));
    }
}
