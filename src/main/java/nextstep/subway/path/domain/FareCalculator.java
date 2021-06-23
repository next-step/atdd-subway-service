package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.age_policy.AgeFarePolicy;
import nextstep.subway.path.domain.age_policy.BasicAgeFarePolicy;
import nextstep.subway.path.domain.age_policy.ChildAgeFarePolicy;
import nextstep.subway.path.domain.age_policy.TeenAgeFarePolicy;
import nextstep.subway.path.domain.distance_policy.BasicDistanceFarePolicy;
import nextstep.subway.path.domain.distance_policy.DistanceFarePolicy;
import nextstep.subway.path.domain.distance_policy.Over10KmDistanceFarePolicy;
import nextstep.subway.path.domain.distance_policy.Over50KmDistanceFarePolicy;

public class FareCalculator {

    private AgeFarePolicy ageFarePolicy;
    private DistanceFarePolicy distanceFarePolicy;

    public FareCalculator() {
    }

    public FareCalculator(int distance, int age) {
        ageFarePolicy = setAgeFarePolicy(age);
        distanceFarePolicy = setDistanceFarePolicy(distance);
    }

    private AgeFarePolicy setAgeFarePolicy(int age) {
        if (6 <= age && age < 13) {
            return new ChildAgeFarePolicy();
        }

        if (13 <= age && age < 19) {
            return new TeenAgeFarePolicy();
        }

        return new BasicAgeFarePolicy();
    }

    private DistanceFarePolicy setDistanceFarePolicy(int distance) {
        if (distance <= 10) {
            return new BasicDistanceFarePolicy();
        }

        if (distance <= 50) {
            return new Over10KmDistanceFarePolicy(distance);
        }

        return new Over50KmDistanceFarePolicy(distance);
    }

    public int calculate() {
        int fare =  distanceFarePolicy.calculateByDistance();
        fare = ageFarePolicy.calculateByAge(fare);
        return fare;
    }
}
