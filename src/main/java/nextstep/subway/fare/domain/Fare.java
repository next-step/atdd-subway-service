package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.Path;
import nextstep.subway.fare.utils.FareCalculator;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final int value;

    public static Fare of(Path path) {
        return FareCalculator.calculate(path);
    }

    public Fare() {
        this.value = BASIC_FARE;
    }

    private Fare(int value) {
        this.value = value;
    }

    public Fare add(int amount) {
        return new Fare(value + amount);
    }

    public Fare subtract(int amount) {
        return new Fare(value - amount);
    }

    public Fare multiplyBy(double ratio) {
        return new Fare((int) (value * ratio));
    }

    public int getValue() {
        return value;
    }
}
