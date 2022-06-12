package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {
        // Distance 에서 기본 생성자는 JPA 용도이므로 로직에서 사용하지 마세요.
    }

    public Distance(int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 양수값 이어야 합니다.");
        }

        this.distance = distance;
    }

    public Distance subtract(Distance newDistance) {
        validateIfNull(newDistance);
        return new Distance(this.distance - newDistance.distance);
    }

    public Distance merge(Distance newDistance) {
        validateIfNull(newDistance);
        return new Distance(this.distance + newDistance.distance);
    }

    private void validateIfNull(Distance newDistance) {
        if (newDistance == null) {
            throw new IllegalArgumentException("거리 값이 존재하지 않습니다.");
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
