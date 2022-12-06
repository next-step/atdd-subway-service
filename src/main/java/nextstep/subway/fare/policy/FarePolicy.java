package nextstep.subway.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;

public class FarePolicy {
    private final DistanceSurchargePolicy distanceSurchargePolicy;
    private final MemberDiscountPolicy memberDiscountPolicy;
    private final Fare lineSurcharge;

    private FarePolicy(DistanceSurchargePolicy distanceSurchargePolicy, MemberDiscountPolicy memberDiscountPolicy, Fare lineSurcharge) {
        this.distanceSurchargePolicy = distanceSurchargePolicy;
        this.memberDiscountPolicy = memberDiscountPolicy;
        this.lineSurcharge = lineSurcharge;
    }

    public static FarePolicy of(LoginMember member, Path path) {
        return new FarePolicy(DistanceSurchargePolicy.from(path.distance()), MemberDiscountPolicy.from(member), path.maxLineSurcharge());
    }

    public Fare calculateFare() {
        return memberDiscountPolicy.discountFare(distanceSurchargePolicy.calculateFare().add(lineSurcharge));
    }
}
