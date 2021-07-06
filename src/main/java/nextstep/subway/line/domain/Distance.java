package nextstep.subway.line.domain;

import nextstep.subway.line.exception.BelowZeroDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        validateConstructorArgument(value);
        this.value = value;
    }

    public Distance add(Distance distance) {
        validateAddArgument(distance);
        return new Distance(value + distance.value);
    }

    public Distance minus(Distance distance) {
        validateMinusArgument(distance);
        return new Distance(value - distance.value);
    }

    public int getValue() {
        return value;
    }

    public boolean isLessThan(Distance distance) {
        return this.value <= distance.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    private void validateConstructorArgument(int value) {
        if (value <= 0) {
            throw new BelowZeroDistanceException("거리는 0이하가 될 수 없습니다.");
        }
    }

    private void validateAddArgument(Distance distance) {
        if (distance.value <= 0) {
            throw new BelowZeroDistanceException("0 이하의 값은 더할 수 없습니다.");
        }
    }

    private void validateMinusArgument(Distance distance) {
        if(this.value - distance.value <= 0) {
            throw new BelowZeroDistanceException(format("거리는 0보다 커야 하므로, %s 보다 더 큰 %s를 뺄 수 없습니다.",value, distance.value));
        }
    }
}
