package nextstep.subway.path.additionalfarepolicy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.additionalfarepolicy.memberfarepolicy.MemberDiscountPolicy;

public interface AdditionalFarePolicy {
    Fare calculate(Fare fare, Distance distance, MemberDiscountPolicy memberDiscountPolicy);
}
