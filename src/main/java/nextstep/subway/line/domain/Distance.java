package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;
    private static final String CAN_NOT_LESS_THAN_ZERO = "거리는 0이하의 값일 수 없습니다.";

    @Column(name = "distance", nullable = false)
    private int value;

    protected Distance() {

    }

    public Distance(int distance) {
        validate(distance);
        this.value = distance;
    }

    private void validate(int value) {
        if (value <= MIN_DISTANCE) {
            throw new IllegalArgumentException(CAN_NOT_LESS_THAN_ZERO);
        }
    }

    public Distance subtract(Distance distance) {
        return new Distance(this.value - distance.value);
    }

    public Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    public boolean isOver(Distance distance) {
        return this.value > distance.value;
    }

    public int value() {
        return this.value;
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
}
