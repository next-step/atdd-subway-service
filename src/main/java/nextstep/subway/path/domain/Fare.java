package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public class Fare {
    private static final int BASIC_FARE = 1_250;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(double distance, Lines lines, AgeDiscountPolicy ageDiscountPolicy) {
        return new Fare(calFare(distance, lines, ageDiscountPolicy));
    }

    public int value() {
        return fare;
    }

    private static int calFare(double distance, Lines lines, AgeDiscountPolicy ageDiscountPolicy) {

        return ageDiscountPolicy.discount(
                BASIC_FARE + FareDistance.calAdditionalFare(distance) + lines.getMaxAdditionalFare());
    }
}
