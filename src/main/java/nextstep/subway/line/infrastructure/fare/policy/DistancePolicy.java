package nextstep.subway.line.infrastructure.fare.policy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.fare.policy.DistanceFarePolicy;
import nextstep.subway.line.domain.fare.policycondition.DistancePolicyCondition;
import nextstep.subway.line.infrastructure.fare.policycondition.DefaultDistanceFareCondition;
import nextstep.subway.line.infrastructure.fare.policycondition.DistanceFareFirstSectionCondition;
import nextstep.subway.line.infrastructure.fare.policycondition.DistanceFareSecondSectionCondition;

public class DistancePolicy extends DistanceFarePolicy {

    private static final List<DistancePolicyCondition> CONDITIONS = Arrays.asList(
        DefaultDistanceFareCondition.of(),
        DistanceFareFirstSectionCondition.of(),
        DistanceFareSecondSectionCondition.of()
    );

    @Override
    public List<DistancePolicyCondition> getConditions() {
        return CONDITIONS;
    }
}
