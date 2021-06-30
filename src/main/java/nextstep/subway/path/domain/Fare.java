package nextstep.subway.path.domain;

import nextstep.subway.path.utils.FareCalculator;

public class Fare {

    public static final int FREE = 0;

    private static final int YOUTH_DISCOUNT = 350;
    private static final double TEENAGER_PAY_RATE = 0.8;
    private static final double CHILD_PAY_RATE = 0.5;

    private final int value;

    public static Fare of(Path path) {
        return FareCalculator.calculate(path);
    }

    public Fare(int value) {
        this.value = value;
    }

    public int adultFare() {
        return value;
    }

    public int teenagerFare() {
        return (int) ((value - YOUTH_DISCOUNT) * TEENAGER_PAY_RATE);
    }

    public int childFare() {
        return (int) ((value - YOUTH_DISCOUNT) * CHILD_PAY_RATE);
    }
}
