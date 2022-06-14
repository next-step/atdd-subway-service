package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.age.AgeFareType;
import nextstep.subway.fare.domain.age.AgePolicy;
import nextstep.subway.fare.domain.distance.DistanceFareType;
import nextstep.subway.fare.domain.distance.DistancePolicy;
import nextstep.subway.path.domain.Path;

public class Fare {
    private int value;

    public Fare(int value) {
        this.value = value;
    }

    public static Fare of(Path path, LoginMember loginMember) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(path.getDistance());
        Fare fareByDistance = new Fare(distancePolicy.calculate(path.getDistance()));

        AgePolicy agePolicy = AgeFareType.getAgePolicy(loginMember.getAge());
        Fare fareByAge = new Fare(agePolicy.calculate());

        Fare totalFare = fareByAge.plus(fareByDistance);
        totalFare = totalFare.plus(new Fare(path.getMostExpensiveLineFare()));
        totalFare = new Fare((int) (totalFare.getValue() * (100 - agePolicy.discountRate()) * 0.01));
        return totalFare;
    }

    public int getValue() {
        return value;
    }

    public Fare plus(Fare fare) {
        return new Fare(value + fare.getValue());
    }
}
