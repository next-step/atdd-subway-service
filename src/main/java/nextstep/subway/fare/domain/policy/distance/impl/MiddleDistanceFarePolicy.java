package nextstep.subway.fare.domain.policy.distance.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class MiddleDistanceFarePolicy implements DistanceFarePolicy {

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        double discountRate = ageFarePolicy.getDiscountRate();
        int additionalDistance = distance - freeDistance;
        if (additionalDistance < 0) {
            return 0;
        }

        return (int) ((Math.ceil((additionalDistance - 1) / 5) + 1) * (defaultAddFare - discountRate));
    }

    @Override
    public boolean isDistance(int distance) {
        return distance > 10 && distance <= 50;
    }

}
