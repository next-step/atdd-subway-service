package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DistancePolicyFactory {
    public static FarePolicy findPolicy(int totalDistance) {
        if (totalDistance > 10 && totalDistance < 50) {
            return new OverTenAndUnderFiftyPolicy(totalDistance);
        }

        if (totalDistance > 50) {
            return new OverFiftyPolicy(totalDistance);
        }

        return new DefaultDistancePolicy();
    }
}
