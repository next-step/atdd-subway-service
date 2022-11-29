package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDistanceException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    public static final Distance ZERO = new Distance(0);

    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        this.value = value;
    }

    public Distance(double value) {
        this(Double.valueOf(value).intValue());
    }

    private Distance subtract(int newDistance) {
        if (value <= newDistance) {
            throw new InvalidDistanceException();
        }
        return new Distance(value - newDistance);
    }

    public Distance subtract(Distance distance) {
        return subtract(distance.value);
    }

    public Distance add(Distance distance) {
        return plus(distance.value);
    }

    private Distance plus(int value) {
        return new Distance(this.value + value);
    }

    public double toDouble() {
        return this.value;
    }

    public int toInt() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
