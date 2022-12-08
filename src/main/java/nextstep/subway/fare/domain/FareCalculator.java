package nextstep.subway.fare.domain;

public class FareCalculator {
    public static final int BASIC_FARE = 1250;
    public static final int MIN_ADULT_AGE = 19;

    public static int distanceCalculate(int distance) {
        return ExtraFare.calculate(distance) + BASIC_FARE;
    }

    public static int distanceWithAgeCalculate(int distance, int age) {
        int fare = ExtraFare.calculate(distance) + BASIC_FARE;
        AgeFarePolicy policy = AgeFarePolicy.findByAge(age);
        return policy.discount(fare);
    }
}
