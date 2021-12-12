package nextstep.subway.line.infrastructure.fare.policy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.fare.policycondition.AgeDiscountFareCondition;
import nextstep.subway.line.domain.fare.policy.AgeDiscountFarePolicy;
import nextstep.subway.line.infrastructure.fare.policycondition.ChildDiscountCondition;
import nextstep.subway.line.infrastructure.fare.policycondition.YouthDiscountCondition;

public class AgeDiscountPolicy extends AgeDiscountFarePolicy {

    private static final List<AgeDiscountFareCondition> CONDITIONS = Arrays.asList(
        YouthDiscountCondition.of(),
        ChildDiscountCondition.of()
    );

    @Override
    protected List<AgeDiscountFareCondition> getConditions() {
        return CONDITIONS;
    }
}
