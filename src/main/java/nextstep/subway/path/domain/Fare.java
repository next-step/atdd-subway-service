package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    public static final Fare DEFAULT_FARE = Fare.of(1250);

    private static final int DEFAULT_OVER_FARE = 0;
    private static final int SHORT_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;
    private static final int UNIT_SHORT_DISTANCE = 5;
    private static final int UNIT_LONG_DISTANCE = 8;
    private static final int UNIT_FARE = 100;

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
        int fare = DEFAULT_OVER_FARE;
        if (distance > SHORT_DISTANCE && distance <= LONG_DISTANCE) {
            fare += calculateOverFare(distance - SHORT_DISTANCE, UNIT_SHORT_DISTANCE);
        }
        if (distance > LONG_DISTANCE) {
            fare += calculateOverFare(distance - LONG_DISTANCE, UNIT_LONG_DISTANCE) + calculateOverFare(LONG_DISTANCE - SHORT_DISTANCE, UNIT_SHORT_DISTANCE);
        }
        return fare;
    }

    private static int calculateOverFare(int distance, int unitDistance) {
        return (int) ((Math.ceil(((distance) - 1) / unitDistance) + 1) * UNIT_FARE);
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
