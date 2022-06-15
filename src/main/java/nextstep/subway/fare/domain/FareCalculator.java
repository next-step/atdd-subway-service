package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Customer;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.age.AgeFareType;
import nextstep.subway.fare.domain.distance.DistanceFareType;
import nextstep.subway.fare.domain.distance.DistancePolicy;
import nextstep.subway.path.domain.Path;

public class FareCalculator {

    public static Fare calculate(Path path, Customer customer) {
        if (customer instanceof LoginMember) {
            return calculatorByLoggedIn(customer, path);
        }

        return calculatorByNonLoggedIn(customer, path);
    }

    private static Fare calculatorByLoggedIn(Customer customer, Path path) {
        LoginMember loginMember = (LoginMember) customer;
        Fare distanceFare = calculatePathFare(path);
        return AgeFareType.calculateTotalFare(loginMember.getAge(), distanceFare);
    }

    private static Fare calculatorByNonLoggedIn(Customer customer, Path path) {
        Fare distanceFare = calculatePathFare(path);
        Fare totalFare = distanceFare.plus(new Fare(customer.DEFAULT_FARE));
        totalFare = totalFare.plus(new Fare(path.getMostExpensiveLineFare()));
        return totalFare;
    }

    private static Fare calculatePathFare(Path path) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(path.getDistance());
        Fare distanceFare = new Fare(distancePolicy.calculate(path.getDistance()));
        return distanceFare.plus(new Fare(path.getMostExpensiveLineFare()));
    }
}
