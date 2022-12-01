package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public static Distance from(double weight) {
        return new Distance((int)weight);
    }

    public static Distance zero() {
        return new Distance(0);
    }

    public Distance sum(Distance other) {
        return new Distance(distance + other.distance);
    }

    public Distance sub(Distance other) {
        validateSub(other);
        return new Distance(distance - other.distance);
    }

    private void validateSub(Distance other) {
        if (this.distance <= other.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public int value() {
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
        Distance distance1 = (Distance)o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
