package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public class Fare {
    private static final int BASIC_FARE = 1_250;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(double distance, Lines lines, AgeDiscountPolicy ageDiscountPolicy) {
        int calculatedFare = ageDiscountPolicy.discount(
                BASIC_FARE + FareDistance.calAdditionalFare(distance) + lines.getMaxAdditionalFare());
        return new Fare(calculatedFare);
    }

    public int value() {
        return fare;
    }

}
