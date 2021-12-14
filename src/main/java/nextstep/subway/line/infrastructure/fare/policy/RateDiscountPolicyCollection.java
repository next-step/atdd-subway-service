package nextstep.subway.line.infrastructure.fare.policy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.fare.policycondition.RateDiscountFareCondition;
import nextstep.subway.line.domain.fare.policy.RateDiscountFarePolicy;
import nextstep.subway.line.infrastructure.fare.policycondition.AgeRatePolicyCondition;

public class RateDiscountPolicyCollection extends RateDiscountFarePolicy {

    private static final List<RateDiscountFareCondition> CONDITIONS = Arrays.asList(
        AgeRatePolicyCondition.values());

    @Override
    protected List<RateDiscountFareCondition> getConditions() {
        return CONDITIONS;
    }
}
