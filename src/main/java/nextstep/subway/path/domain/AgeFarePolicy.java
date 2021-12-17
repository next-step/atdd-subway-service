package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;

public enum AgeFarePolicy {
    BASIC(0, 0, 0, 1),
    CHILD(6, 12, 350, 0.5),
    TEENAGER(13, 18, 350, 0.8);

    private int min;
    private int max;
    private Fare deduction;
    private double discountRate;

    AgeFarePolicy(int min, int max, int deduction, double discountRate) {
        this.min = min;
        this.max = max;
        this.deduction = Fare.of(deduction);
        this.discountRate = discountRate;
    }

    public static Fare discount(int age, Fare fare) {
        return findPolicy(age).discountFare(fare);
    }

    private Fare discountFare(Fare fare) {
        return fare.minus(deduction).multiply(discountRate);
    }

    private static AgeFarePolicy findPolicy(int age) {
        return Arrays.stream(values())
                .filter(distanceFarePolicy -> distanceFarePolicy.min <= age && distanceFarePolicy.max >= age)
                .findFirst()
                .orElse(BASIC);
    }
}
