package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDistanceException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private int value;

    public Distance(int value) {
        this.value = value;
    }

    protected Distance() {
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
