package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public class FareAgeCalculator {

    private static final int DISCOUNT_FARE = 350;

    public static Fare calculate(int fare, int age) {
        FareAge fareAge = FareAge.findByAge(age);

        if (fareAge == FareAge.ADULT) {
            return Fare.from(calculateOverFare(fare, fareAge.getRate(), 0));
        }

        return Fare.from(calculateOverFare(fare, fareAge.getRate(), DISCOUNT_FARE));
    }

    private static int calculateOverFare(int fare, double rate, int discountFare) {
        int a = 1;
        return (int) ((fare - discountFare) * rate);
    }
}
