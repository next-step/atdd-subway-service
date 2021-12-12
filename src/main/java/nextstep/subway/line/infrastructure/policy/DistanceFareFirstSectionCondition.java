package nextstep.subway.line.infrastructure.policy;

import nextstep.subway.line.application.policy.SubwayDistancePolicyCondition;
import nextstep.subway.line.domain.fare.Money;

public class DistanceFareFirstSectionCondition implements SubwayDistancePolicyCondition {

    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int PREMIUM_DISTANCE = 5;

    public static DistanceFareFirstSectionCondition of() {
        return new DistanceFareFirstSectionCondition();
    }

    @Override
    public boolean isSatisfiedBy(int distance) {
        return distance > MIN_DISTANCE;
    }

    @Override
    public Money calculateFare(int distance) {
        if (distance > MAX_DISTANCE) {
            distance = MAX_DISTANCE;
        }
        return Money.wons((int) (
            (Math.floor((double) (distance - MIN_DISTANCE - 1) / PREMIUM_DISTANCE) + 1)
                * ADDITIONAL_FARE));
    }
}
