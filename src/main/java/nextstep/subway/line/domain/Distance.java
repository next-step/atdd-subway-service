package nextstep.subway.line.domain;

import nextstep.subway.line.exception.OutOfDistanceRangeException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int ZERO = 0;
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validDistance(distance);
        this.distance = distance;
    }

    private void validDistance(int distance) {
        if (distance < ZERO) {
            throw new OutOfDistanceRangeException("거리는 0이상이어야 합니다.");
        }
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public boolean isShorter(Distance newDistance) {
        return this.distance <= newDistance.distance;
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
