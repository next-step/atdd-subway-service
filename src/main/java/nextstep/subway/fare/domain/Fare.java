package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.Path;
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

    private Fare(double value) {
        this((int) value);
    }

    public Fare(int value) { // TODO default로 변경 필요
        this.value = value;
    }

    public Fare subtract(int amount) {
        return new Fare(value - amount);
    }

    public Fare multiplyBy(double ratio) {
        return new Fare(value * ratio);
    }

    public int getValue() {
        return value;
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
