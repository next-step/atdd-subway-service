package nextstep.subway.fare.domain.policy.distance.impl;

import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.DEFAULT_DISTANCE_ADDITIONAL_FARE;
import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.MIDDLE_MAX_DISTANCE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class LongDistanceFarePolicy implements DistanceFarePolicy {

    private static class InstanceHolder {
        private static final LongDistanceFarePolicy instance = new LongDistanceFarePolicy();
    }

    public static LongDistanceFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        double discountRate = ageFarePolicy.getDiscountRate();
        int additionalDistance = distance - MIDDLE_MAX_DISTANCE;
        if (additionalDistance < 0) {
            return 0;
        }

        int middleAdditionalDistanceFare = new MiddleDistanceFarePolicy().calculate(50, ageFarePolicy);
        int longMiddleAdditionalDistanceFare = (int) ((Math.ceil((additionalDistance - 1) / 8) + 1) * (DEFAULT_DISTANCE_ADDITIONAL_FARE - discountRate));

        return middleAdditionalDistanceFare + longMiddleAdditionalDistanceFare;
    }

    @Override
    public boolean includeDistance(int distance) {
        return distance >= MIDDLE_MAX_DISTANCE;
    }

}
