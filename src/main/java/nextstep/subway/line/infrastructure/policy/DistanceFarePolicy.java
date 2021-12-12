package nextstep.subway.line.infrastructure.policy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.application.policy.SubwayDistancePolicyCondition;
import nextstep.subway.line.application.policy.SubwayDistanceFarePolicy;

public class DistanceFarePolicy implements SubwayDistanceFarePolicy {

    private static final List<SubwayDistancePolicyCondition> CONDITIONS = Arrays.asList(
        DefaultDistanceFareCondition.of(),
        DistanceFareFirstSectionCondition.of(),
        DistanceFareSecondSectionCondition.of()
    );

    @Override
    public List<SubwayDistancePolicyCondition> getConditions() {
        return CONDITIONS;
    }
}
