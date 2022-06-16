package nextstep.subway.fare.domain.policy.distance.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class DefaultDistanceFarePolicy implements DistanceFarePolicy {

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        return 0;
    }

    @Override
    public boolean isDistance(int distance) {
        return distance <= 10;
    }

}
