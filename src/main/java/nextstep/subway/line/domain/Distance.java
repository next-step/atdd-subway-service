package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String DISTANCE_IS_LESS_THAN_MIN_DISTANCE_ERROR_MESSAGE = "거리의 길이는 1보다 길어야합니다. distance=%s";
    private static final int MIN = 1;

    @Column(name = "distance", nullable = false)
    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        validateDistance(distance);
        return new Distance(distance);
    }

    public static Distance createEmpty() {
        return new Distance();
    }

    public static Distance merge(Distance upDistance, Distance downDistance) {
        return Distance.from(upDistance.distance + downDistance.distance);
    }

    public boolean isGreaterThanOrEqualTo(Distance distance) {
        return this.distance >= distance.getValue();
    }

    public boolean isLessThanOrEqualTo(Distance distance) {
        return this.distance <= distance.getValue();
    }

    public Distance minus(Distance distance) {
        return Distance.from(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public int getValue() {
        return distance;
    }

    private static void validateDistance(int distance) {
        if (distance < MIN) {
            throw new IllegalArgumentException(String.format(DISTANCE_IS_LESS_THAN_MIN_DISTANCE_ERROR_MESSAGE, distance));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Distance distance1 = (Distance)o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
