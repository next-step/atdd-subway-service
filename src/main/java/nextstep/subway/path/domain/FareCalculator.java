package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {

    public static final int FIRST_ADDITION_BASE_DISTANCE = 10;
    public static final int FIRST_ADDITION_INTERVAL = 5;
    public static final int SECOND_ADDITION_BASE_DISTANCE = 50;
    public static final int SECOND_ADDITION_INTERVAL = 8;

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
        return fare.plus(calculateAdditionFare(distance, surcharge))
            .minus(farePolicy.getDiscountAmount())
            .applyDiscountRate(farePolicy.getDiscountRate());
    }

    private int calculateAdditionFare(int distance, int surcharge) {
        if (isFirstAdditionDistance(distance)) {
            return surcharge
                + calculateOverFare(distance - FIRST_ADDITION_BASE_DISTANCE, FIRST_ADDITION_INTERVAL);
        }
        if (isSecondAdditionDistance(distance)) {
            return surcharge
                + calculateOverFare(SECOND_ADDITION_BASE_DISTANCE - FIRST_ADDITION_BASE_DISTANCE, FIRST_ADDITION_INTERVAL)
                + calculateOverFare(distance - SECOND_ADDITION_BASE_DISTANCE, SECOND_ADDITION_INTERVAL);
        }
        return surcharge;
    }

    private boolean isFirstAdditionDistance(int distance) {
        return distance > FIRST_ADDITION_BASE_DISTANCE && distance <= SECOND_ADDITION_BASE_DISTANCE;
    }

    private boolean isSecondAdditionDistance(int distance) {
        return distance > SECOND_ADDITION_BASE_DISTANCE;
    }

    private int calculateOverFare(int distance, int interval) {
        return (((distance - 1) / interval) + 1) * 100;
    }
}
