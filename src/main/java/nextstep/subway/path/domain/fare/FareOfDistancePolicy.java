package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public class FareOfDistancePolicy implements FareCalculator {

    private final int distance;

    public FareOfDistancePolicy(final int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(Fare fare) {
        return FareOfDistancePolicyFactory.calculate(distance);
    }
}
