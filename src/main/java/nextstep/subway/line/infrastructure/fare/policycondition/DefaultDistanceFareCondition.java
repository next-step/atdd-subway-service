package nextstep.subway.line.infrastructure.fare.policycondition;

import nextstep.subway.line.domain.policycondition.DistancePolicyCondition;
import nextstep.subway.line.domain.Money;

public class DefaultDistanceFareCondition implements DistancePolicyCondition {

    public static final Money DEFAULT_FARE = Money.won(1250);

    public static DefaultDistanceFareCondition of() {
        return new DefaultDistanceFareCondition();
    }

    @Override
    public Money calculateFare(int distance) {
        return DEFAULT_FARE;
    }
}
