package nextstep.subway.fare.domain.policy.distance;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public interface DistanceFarePolicy {

    int defaultAddFare = 100;
    int freeDistance = 10;
    int middleDistance = 50;

    int calculate(int distance, AgeFarePolicy ageFarePolicy);
    boolean isDistance(int distance);

}
