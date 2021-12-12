package nextstep.subway.line.infrastructure.fare.policycondition;

import nextstep.subway.line.domain.policycondition.DistancePolicyCondition;
import nextstep.subway.line.domain.Money;

public class DistanceFareSecondSectionCondition implements DistancePolicyCondition {

    private static final int MIN_DISTANCE = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int PREMIUM_DISTANCE = 8;

    public static DistanceFareSecondSectionCondition of() {
        return new DistanceFareSecondSectionCondition();
    }

    public boolean isSatisfiedBy(int distance) {
        return distance > MIN_DISTANCE;
    }

    @Override
    public Money calculateFare(int distance) {
        if (!isSatisfiedBy(distance)) {
            return Money.ZERO;
        }
        
        return calculate(distance, MIN_DISTANCE, PREMIUM_DISTANCE, ADDITIONAL_FARE);
    }
}
