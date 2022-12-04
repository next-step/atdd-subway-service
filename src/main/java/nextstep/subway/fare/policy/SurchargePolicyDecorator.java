package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;

public class SurchargePolicyDecorator implements SurchargePolicy {
    private final SurchargePolicy surchargePolicy;

    public SurchargePolicyDecorator(SurchargePolicy surchargePolicy) {
        this.surchargePolicy = surchargePolicy;
    }

    @Override
    public Fare calculateFare() {
        return surchargePolicy.calculateFare();
    }
}
