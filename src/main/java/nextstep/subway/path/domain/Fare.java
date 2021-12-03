package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    public static final Fare DEFAULT_FARE = Fare.of(1250);

    private final int value;

    public static Fare of(int value) {
        return new Fare(value);
    }

    public static Fare extra(int lineFare, int distance) {
        DistanceFarePolicy distancePolicy = new DistanceFarePolicy();
        return DEFAULT_FARE.plus(lineFare + distancePolicy.calculate(distance));
    }

    private Fare(int value) {
        this.value = value;
    }

    private Fare plus(int lineFare) {
        return new Fare(this.value + lineFare);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
