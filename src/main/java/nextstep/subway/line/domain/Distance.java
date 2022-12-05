package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int MIN_VALUE = 1;

    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MIN_VALUE) {
            throw new IllegalArgumentException("구간거리는 " + MIN_VALUE + "보다 크거나 같아야합니다.");
        }
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.value());
    }

    public Distance subtract(Distance distance) {
        return Distance.from(this.distance - distance.value());
    }

    public int value() {
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
