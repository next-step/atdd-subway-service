package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.ShortestPath;

public class FareCalculator {
    private final LoginMember loginMember;
    private final ShortestPath shortestPath;

    private FareCalculator(LoginMember loginMember, ShortestPath shortestPath) {
        this.loginMember = loginMember;
        this.shortestPath = shortestPath;
    }

    public static FareCalculator of(LoginMember loginMember, ShortestPath shortestPath) {
        return new FareCalculator(loginMember, shortestPath);
    }

    public Fare calculate() {
        Fare totalFare = DistanceFare.findDistanceFare(shortestPath.getDistance()).additionalFare(shortestPath.getDistance());
        Fare lineMaxPathFare = shortestPath.getMaxPathFare();
        totalFare = totalFare.plus(lineMaxPathFare);

        if(loginMember.hasAuthentication()) {
            totalFare = AgeDiscountFare.findAgeDiscount((loginMember.getAge())).calculateDiscount(totalFare);
        }

        return totalFare;
    }
}
