package nextstep.subway.path.domain;

public class FareCalculator {

    public static final int MINIMUM_FARE = 1_250;
    public static final int ADD_FARE = 100;
    public static final int ONE_LEVEL_STANDARD_DISTANCE = 10;
    public static final int TWO_LEVEL_STANDARD_DISTANCE = 50;
    public static final int ONE_LEVEL_ADD_FARE = 5;
    public static final int TWO_LEVEL_ADD_FARE = 8;

    public static Fare calculateFare(int distance) {
        int charge = MINIMUM_FARE;
        if (isLessThanTen(distance)) {
            return new Fare(charge);
        }

        if (isLessThanOrEqualToFifty(distance)) {
            charge += calculateOverFare(distance - ONE_LEVEL_STANDARD_DISTANCE, ONE_LEVEL_ADD_FARE);
            return new Fare(charge);
        }

        int stepOneMaxDistance = TWO_LEVEL_STANDARD_DISTANCE - ONE_LEVEL_STANDARD_DISTANCE;
        charge += calculateOverFare(stepOneMaxDistance, ONE_LEVEL_ADD_FARE);
        charge += calculateOverFare(distance - TWO_LEVEL_STANDARD_DISTANCE, TWO_LEVEL_ADD_FARE);
        return new Fare(charge);
    }

    private static boolean isLessThanOrEqualToFifty(int distance) {
        return distance <= TWO_LEVEL_STANDARD_DISTANCE;
    }

    private static boolean isLessThanTen(int distance) {
        return distance < ONE_LEVEL_STANDARD_DISTANCE;
    }

    private static int calculateOverFare(int distance, int standard) {
        return (int)((Math.ceil((distance - 1) / standard) + 1) * ADD_FARE);
    }

    public static Fare discountFare(int fare, int age) {
        DiscountPolicy discountPolicy = DiscountPolicy.of(age);
        int discountFare = (int)discountPolicy.reduce(fare);
        return new Fare(discountFare);
    }

    private static double calculateDiscountFare(int fare, double discount) {
        return fare * discount;
    }
}
