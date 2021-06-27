package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {

    private final FarePolicy farePolicy;

    public FareCalculator(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public FareCalculator(LoginMember loginMember) {
        this.farePolicy = FarePolicy.get(loginMember.getAge());
    }

    public Fare calculate(ShortestPath shortestPath) {
        return this.calculate(shortestPath.getDistance(), shortestPath.getSurcharge());
    }

    protected Fare calculate(int distance, int surcharge) {
        Fare fare = new Fare(farePolicy.getDefaultAmount());
        if (fare.isFree()) {
            return fare;
        }
        int addFare = calculateAdditionFare(distance, surcharge);
        return fare.plus(addFare)
            .minus(farePolicy.getDiscountAmount())
            .applyDiscountRate(farePolicy.getDiscountRate());
    }


    private int calculateAdditionFare(int distance, int surcharge) {
        if (distance > 10 && distance <= 50) {
            return surcharge + calculateOverFare(5, distance - 10);
        }
        if (distance > 50) {
            return surcharge + (calculateOverFare(5, 50 - 10) + calculateOverFare(8, distance - 50));
        }
        return surcharge;
    }

    private int calculateOverFare(int interval, int distance) {
        return ((distance - 1) / interval + 1) * 100;
    }
}
