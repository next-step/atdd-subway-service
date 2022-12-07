package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.policy.AdditionalFarePolicy;
import nextstep.subway.path.domain.policy.ChargeFarePolicy;
import nextstep.subway.path.domain.policy.DiscountPolicy;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private final ChargeFarePolicy chargeFarePolicy;
    private final AdditionalFarePolicy additionalFarePolicy;
    private final DiscountPolicy discountPolicy;

    public FareCalculator(ChargeFarePolicy chargeFarePolicy, AdditionalFarePolicy additionalFarePolicy, DiscountPolicy discountPolicy) {
        this.chargeFarePolicy = chargeFarePolicy;
        this.additionalFarePolicy = additionalFarePolicy;
        this.discountPolicy = discountPolicy;
    }

    public int calculate(Sections sections, Path path, Member member) {
        int fare = chargeFarePolicy.charge(path.getDistance());
        fare += additionalFarePolicy.addFare(sections, path.getStationPaths());
        fare = discountPolicy.discount(fare, member.getAge());

        return fare;
    }
}