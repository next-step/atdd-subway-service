package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public Distance add(Distance other) {
        return new Distance(distance + other.distance);
    }

    public Distance subtract(Distance other) {
        return new Distance(distance - other.distance);
    }

    private void validate(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0이하가 될 수 없습니다.");
        }
    }

    public int get() {
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
