package nextstep.subway.line.application.policy;

import java.util.List;
import nextstep.subway.line.domain.fare.Money;

public interface SubwayDistanceFarePolicy {

    default Money calculateFare(int distance) {
        Money result = Money.ZERO;
        for (SubwayDistancePolicyCondition each : getConditions()) {
            if (each.isSatisfiedBy(distance)) {
                result = result.plus(each.calculateFare(distance));
            }
        }

        return result;
    }

    List<SubwayDistancePolicyCondition> getConditions();
}
