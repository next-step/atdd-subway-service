package nextstep.subway.path.domain;

public class FareCalculator {

    public static final int MINIMUM_FARE = 1_250;

    public static Fare calculateFare(int distance) {
        int amount = MINIMUM_FARE + DistancePolicy.getOverFare(distance);
        return new Fare(amount);
    }

    public static Fare discountFare(int fare, int age) {
        DiscountPolicy discountPolicy = DiscountPolicy.of(age);
        int discountFare = (int)discountPolicy.reduce(fare);
        return new Fare(discountFare);
    }
}
