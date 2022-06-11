package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int distance;

    public Distance() {
    }

    public Distance(int distance) {
        if (distance <= 0) {
            throw new InvalidDistanceException("구간 거리는 0보다 커야 합니다");
        }
        this.distance = distance;
    }

    public Distance plusDistance(int distance) {
        int plus = this.distance + distance;
        return new Distance(plus);
    }

    public Distance minusDistance(int distance) {
        int minus = this.distance - distance;
        return new Distance(minus);
    }

    public int getDistance() {
        return distance;
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

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
