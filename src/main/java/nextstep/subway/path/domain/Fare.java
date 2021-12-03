package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    public static final Fare DEFAULT_FARE = Fare.of(1250);

    private final int value;

    public static Fare of(int value) {
        return new Fare(value);
    }

    public static Fare extra(int lineFare, int distance) {
        int distanceFare = calculateOverFare(distance);
        return DEFAULT_FARE.plus(lineFare + distanceFare);
    }

    private Fare(int value) {
        this.value = value;
    }

    private static int calculateOverFare(int distance) {
        int fare = 0;
        if (distance > 10 && distance <= 50) {
            fare += (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
        }
        if (distance > 50) {
            fare += (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100) + 800;
        }
        return fare;
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
}
