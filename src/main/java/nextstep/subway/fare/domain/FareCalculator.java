package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
    }

    public Fare calculate(Distance distance) {

        return distanceFarePolicy.calculate(distance);
    }
}
