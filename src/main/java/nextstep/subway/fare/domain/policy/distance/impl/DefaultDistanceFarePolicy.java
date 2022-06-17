package nextstep.subway.fare.domain.policy.distance.impl;

import static nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType.DEFAULT_MAX_DISTANCE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class DefaultDistanceFarePolicy implements DistanceFarePolicy {

    private static class InstanceHolder {
        private static final DefaultDistanceFarePolicy instance = new DefaultDistanceFarePolicy();
    }

    public static DefaultDistanceFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        return 0;
    }

    @Override
    public boolean includeDistance(int distance) {
        return distance <= DEFAULT_MAX_DISTANCE;
    }

}
