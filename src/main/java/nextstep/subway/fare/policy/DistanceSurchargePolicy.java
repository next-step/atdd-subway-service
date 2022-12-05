package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

class DistanceSurchargePolicy {
    private final Distance distance;

    private DistanceSurchargePolicy(Distance distance) {
        this.distance = distance;
    }

    public static DistanceSurchargePolicy from(Distance distance) {
        return new DistanceSurchargePolicy(distance);
    }

    public Fare calculateFare() {
        return DistanceSurcharge.calculate(distance);
    }
}
