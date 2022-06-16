package nextstep.subway.fare.domain.policy.distance.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;

public class LongDistanceFarePolicy implements DistanceFarePolicy {

    @Override
    public int calculate(int distance, AgeFarePolicy ageFarePolicy) {
        double discountRate = ageFarePolicy.getDiscountRate();
        int additionalDistance = distance - middleDistance;
        if (additionalDistance < 0) {
            return 0;
        }

        int middleAdditionalDistanceFare = new MiddleDistanceFarePolicy().calculate(50, ageFarePolicy);
        int longMiddleAdditionalDistanceFare = (int) ((Math.ceil((additionalDistance - 1) / 8) + 1) * (defaultAddFare - discountRate));

        return middleAdditionalDistanceFare + longMiddleAdditionalDistanceFare;
    }

    @Override
    public boolean isDistance(int distance) {
        return distance >= 50;
    }

}
