package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int ZERO = 0;

    @Column(nullable = false)
    private int value;

    protected Distance() {
    }

    public Distance(int distance) {
        validate(distance);
        this.value = distance;
    }

    private static void validate(int distance) {
        if (distance <= ZERO) {
            throw new IllegalArgumentException("길이는 0이상 숫자를 입력해야 합니다.");
        }
    }

    public int value() {
        return this.value;
    }

    public Distance subtract(Distance newDistance) {
        return new Distance(this.value - newDistance.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    public void validNewDistance(Distance newDistance) {
        if (value <= newDistance.value) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
