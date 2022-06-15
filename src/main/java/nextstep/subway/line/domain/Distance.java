package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 1;
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance sum(Distance target) {
        return Distance.from(distance + target.distance);
    }

    public Distance minus(Distance target) {
        return Distance.from(distance - target.distance);
    }

    public int getValue() {
        return this.distance;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException("역 간 거리는 0보다 커야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
