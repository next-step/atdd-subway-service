package nextstep.subway.line.domain;

import java.util.Objects;

import static java.lang.String.format;

public class Distance {
    private static final int MINIMUM_DISTANCE = 0;

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validate(distance);

        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(format("최소 거리는 %d 이상이여야 합니다", MINIMUM_DISTANCE));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
