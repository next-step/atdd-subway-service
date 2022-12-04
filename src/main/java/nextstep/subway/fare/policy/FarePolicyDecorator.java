package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;

public class FarePolicyDecorator implements FarePolicy {
    private final FarePolicy farePolicy;

    public FarePolicyDecorator(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    @Override
    public Fare calculateFare() {
        return farePolicy.calculateFare();
    }
}
