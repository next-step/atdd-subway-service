package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public class FareAgeCalculator {

    private static final int DISCOUNT_FARE = 350;

    public static Fare calculate(int fare, int age) {
        FareAge fareAge = FareAge.findByAge(age);
        return Fare.from(calculateFare(fare, fareAge.getRate()));
    }

    private static int calculateFare(int fare, double rate) {
        return (int) ((fare - DISCOUNT_FARE) * rate);
    }
}
