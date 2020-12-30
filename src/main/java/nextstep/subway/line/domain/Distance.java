package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidDistanceValueException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Distance {
    @Transient
    public static final int MIN_DISTANCE_VALUE = 1;

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public Distance minus(Distance thatDistance) {
        return new Distance(this.distance - thatDistance.distance);
    }

    public Distance plus(Distance thatDistance) {
        return new Distance(this.distance + thatDistance.distance);
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE_VALUE) {
            throw new InvalidDistanceValueException("거리는 1 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
