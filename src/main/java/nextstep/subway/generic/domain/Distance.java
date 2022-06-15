package nextstep.subway.generic.domain;

import java.util.Objects;

public class Distance {
    private final int value;

    protected Distance(final int value) {
        validateDistance(value);
        this.value = value;
    }

    private void validateDistance(final int value) {
        if (value < 1) {
            throw new IllegalArgumentException("거리는 1 보다 작을 수 없습니다.");
        }
    }

    public static Distance valueOf(final int value) {
        return new Distance(value);
    }

    public int getValue() {
        return value;
    }

    public boolean isLessThanOrEqualsTo(final Distance distance) {
        return value <= distance.value;
    }

    public Distance minus(final Distance newDistance) {
        return new Distance(value - newDistance.value);
    }

    public boolean isGreaterThanOrEqualsTo(final Distance distance) {
        return value >= distance.value;
    }

    public Distance plus(final Distance distance) {
        return new Distance(value + distance.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distance)) {
            return false;
        }
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }
}
