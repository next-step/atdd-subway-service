package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int MINIMUM_DISTANCE = 1;

    @Column(nullable = false)
    private int distance;

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    protected Distance() {
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance subtract(Distance otherDistance) {
        validateDistanceComparison(otherDistance.getDistance());
        return Distance.from(this.distance - otherDistance.getDistance());
    }

    public Distance sum(Distance otherDistance) {
        return Distance.from(getDistance() + otherDistance.getDistance());
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException("0보다 큰값을 입력해주세요.");
        }
    }

    private void validateDistanceComparison(int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException("기존의 거리보다 좁은 거리를 입력해주세요.");
        }
    }

    public int getDistance() {
        return distance;
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
