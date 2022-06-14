package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Customer;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.age.AgeFareType;
import nextstep.subway.fare.domain.age.AgePolicy;
import nextstep.subway.fare.domain.distance.DistanceFareType;
import nextstep.subway.fare.domain.distance.DistancePolicy;
import nextstep.subway.path.domain.Path;

public class FareCalculator {

    public static Fare of(Path path, Customer customer) {
        if (customer instanceof LoginMember) {
            return calculatorByLoggedIn(customer, path);
        }

        return calculatorByNonLoggedIn(customer, path);
    }

    private static Fare calculatorByLoggedIn(Customer customer, Path path) {
        LoginMember loginMember = (LoginMember) customer;
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(path.getDistance());
        Fare fareByDistance = new Fare(distancePolicy.calculate(path.getDistance()));

        AgePolicy agePolicy = AgeFareType.getAgePolicy(loginMember.getAge());
        Fare totalFare = fareByDistance.plus(new Fare(agePolicy.calculate()));
        totalFare = totalFare.plus(new Fare(path.getMostExpensiveLineFare()));
        totalFare = new Fare((int) (totalFare.getValue() * (100 - agePolicy.discountRate()) * 0.01));
        return totalFare;
    }

    private static Fare calculatorByNonLoggedIn(Customer customer, Path path) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(path.getDistance());
        Fare fareByDistance = new Fare(distancePolicy.calculate(path.getDistance()));

        Fare totalFare = fareByDistance.plus(new Fare(customer.DEFAULT_FARE));
        totalFare = totalFare.plus(new Fare(path.getMostExpensiveLineFare()));
        return totalFare;
    }
}
