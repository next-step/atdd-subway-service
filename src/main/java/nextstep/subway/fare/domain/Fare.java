package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.Path;
import nextstep.subway.fare.utils.FareCalculator;

public class Fare {

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
}
