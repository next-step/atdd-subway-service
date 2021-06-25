package nextstep.subway.path.domain;

import nextstep.subway.path.domain.age_policy.*;
import nextstep.subway.path.domain.distance_policy.*;

import java.util.Set;

public class FareCalculator {

    private AgeFarePolicy ageFarePolicy;
    private DistanceFarePolicy distanceFarePolicy;

    public FareCalculator() {
    }

    public FareCalculator(int distance, int age) {
        this.ageFarePolicy = setAgeFarePolicy(age);
        this.distanceFarePolicy = setDistanceFarePolicy(distance);
    }

    private AgeFarePolicy setAgeFarePolicy(int age) {
        return AgeFareInformation.ageFarePolicy(age);
    }

    private DistanceFarePolicy setDistanceFarePolicy(int distance) {
        return DistanceFareInformation.distanceFarePolicy(distance);
    }

    public int calculate(Set<Long> lineIds) {
        int additionalFee = LineAdditionalFee.LINE_BUNDANG.maximumAdditionalFee(lineIds);
        int fare = distanceFarePolicy.calculateByDistance() + additionalFee;
        fare = ageFarePolicy.calculateByAge(fare);
        return fare;
    }
}
