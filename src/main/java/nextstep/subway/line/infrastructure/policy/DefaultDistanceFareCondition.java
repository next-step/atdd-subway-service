package nextstep.subway.line.infrastructure.policy;

import nextstep.subway.line.application.policy.SubwayDistancePolicyCondition;
import nextstep.subway.line.domain.fare.Money;

public class DefaultDistanceFareCondition implements SubwayDistancePolicyCondition {

    public static DefaultDistanceFareCondition of() {
        return new DefaultDistanceFareCondition();
    }

    @Override
    public boolean isSatisfiedBy(int distance) {
        return true;
    }

    @Override
    public Money calculateFare(int distance) {
        return Money.wons(1250);
    }
}
