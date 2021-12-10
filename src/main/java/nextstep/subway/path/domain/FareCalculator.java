package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.path.policy.DiscountPolicy;
import nextstep.subway.path.policy.DiscountPolicyByAgeResolver;
import nextstep.subway.path.policy.FarePolicy;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FareCalculator {
    private final FarePolicy farePolicy;
    private final DiscountPolicyByAgeResolver discountPolicyByAgeResolver;

    public FareCalculator(FarePolicy farePolicy, DiscountPolicyByAgeResolver discountPolicyByAgeResolver) {
        this.farePolicy = farePolicy;
        this.discountPolicyByAgeResolver = discountPolicyByAgeResolver;
    }

    public int getFare(LoginMember loginMember, Set<Line> lines, PathResult shortCut) {
        int fare = farePolicy.calculateFare(shortCut.getMaxExtraFare(), (int) shortCut.getWeight());
        if (!loginMember.isEmpty()) {
            DiscountPolicy discountPolicy = discountPolicyByAgeResolver.resolve(loginMember.getAge());
            fare = discountPolicy.apply(fare);
        }
        return fare;
    }
}
