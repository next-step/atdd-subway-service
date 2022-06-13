package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int value;

    protected Distance() {
        // Distance 에서 기본 생성자는 JPA 용도이므로 로직에서 사용하지 마세요.
    }

    public Distance(int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 양수값 이어야 합니다.");
        }

        this.value = distance;
    }

    public Distance subtract(Distance newDistance) {
        validateIfNull(newDistance);
        return new Distance(this.value - newDistance.value);
    }

    public Distance merge(Distance newDistance) {
        validateIfNull(newDistance);
        return new Distance(this.value + newDistance.value);
    }

    private void validateIfNull(Distance newDistance) {
        if (newDistance == null) {
            throw new IllegalArgumentException("거리 값이 존재하지 않습니다.");
        }
    }

    public int getValue() {
        return value;
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
        return value == distance1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
