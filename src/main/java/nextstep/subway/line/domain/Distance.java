package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.ErrorEnum;

@Embeddable
public class Distance {
    private static final int ZERO = 0;

    @Column(nullable = false)
    private int value;

    protected Distance() {
    }

    public Distance(int distance) {
        validate(distance);
        this.value = distance;
    }

    private static void validate(int distance) {
        if (distance <= ZERO) {
            throw new IllegalArgumentException(ErrorEnum.VALID_LINE_LENGTH_GREATER_THAN_ZERO.message());
        }
    }

    public void validNewDistance(Distance newDistance) {
        if (value <= newDistance.value) {
            throw new IllegalArgumentException(ErrorEnum.VALID_GREATER_OR_EQUAL_LENGTH_BETWEEN_STATION.message());
        }
    }

    public int value() {
        return this.value;
    }

    public Distance subtract(Distance newDistance) {
        return new Distance(this.value - newDistance.value);
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

    public Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }
}
