package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

public class DistanceSurchargePolicy extends FarePolicyDecorator {
    private final Distance distance;

    private DistanceSurchargePolicy(FarePolicy farePolicy, Distance distance) {
        super(farePolicy);
        this.distance = distance;
    }

    public static DistanceSurchargePolicy of(FarePolicy farePolicy, Distance distance) {
        return new DistanceSurchargePolicy(farePolicy, distance);
    }

    @Override
    public Fare calculateFare() {
        return super.calculateFare().add(DistanceSurcharge.calculate(distance));
    }
}
