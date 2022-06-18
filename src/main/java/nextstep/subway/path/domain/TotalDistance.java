package nextstep.subway.path.domain;

import java.util.Optional;

public class TotalDistance {
    private int distance;

    public TotalDistance(int distance) {
        this.distance = distance;
    }

    public int calculateOverFare() {
        Optional<FareDistancePolicy> farePolicy = FareDistancePolicy.findFarePolicyByDistance(distance);

        return farePolicy
                .map(policy -> (int) ((Math.ceil((distance - 1) / policy.getDistanceStandardValue()) + 1) * policy.getOverFare()))
                .orElse(0);
    }
}
