package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

public class DistanceSurchargePolicy extends SurchargePolicyDecorator {
    private final Distance distance;

    private DistanceSurchargePolicy(SurchargePolicy surchargePolicy, Distance distance) {
        super(surchargePolicy);
        this.distance = distance;
    }

    public static DistanceSurchargePolicy of(SurchargePolicy surchargePolicy, Distance distance) {
        return new DistanceSurchargePolicy(surchargePolicy, distance);
    }

    @Override
    public Fare calculateFare() {
        return super.calculateFare().add(DistanceSurcharge.calculate(distance));
    }
}
