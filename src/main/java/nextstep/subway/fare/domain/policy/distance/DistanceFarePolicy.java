package nextstep.subway.fare.domain.policy.distance;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public interface DistanceFarePolicy {

    int calculate(int distance, AgeFarePolicy ageFarePolicy);

    boolean includeDistance(int distance);

}
