package nextstep.subway.fare.domain.policy.distance.impl;

import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.DEFAULT_DISTANCE_ADDITIONAL_FARE;
import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.DEFAULT_MAX_DISTANCE;
import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.MIDDLE_MAX_DISTANCE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class MiddleDistanceFarePolicy implements DistanceFarePolicy {

    private static class InstanceHolder {
        private static final MiddleDistanceFarePolicy instance = new MiddleDistanceFarePolicy();
    }

    public static MiddleDistanceFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        double discountRate = ageFarePolicy.getDiscountRate();
        int additionalDistance = distance - DEFAULT_MAX_DISTANCE;
        if (additionalDistance < 0) {
            return 0;
        }

        return (int) ((Math.ceil((additionalDistance - 1) / 5) + 1) * (DEFAULT_DISTANCE_ADDITIONAL_FARE - discountRate));
    }

    @Override
    public boolean includeDistance(int distance) {
        return distance > DEFAULT_MAX_DISTANCE && distance <= MIDDLE_MAX_DISTANCE;
    }

}
