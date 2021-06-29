package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DistancePolicyFactory {
    public static final int TEN_DISTANCE = 10;
    public static final int FIFTY_DISTANCE = 50;

    public static FarePolicy findPolicy(int totalDistance) {
        if (totalDistance > TEN_DISTANCE && totalDistance < FIFTY_DISTANCE) {
            return new OverTenAndUnderFiftyPolicy(totalDistance);
        }

        if (totalDistance > FIFTY_DISTANCE) {
            return new OverFiftyPolicy(totalDistance);
        }

        return new DefaultDistancePolicy();
    }
}
