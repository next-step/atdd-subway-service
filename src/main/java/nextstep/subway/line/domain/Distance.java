package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private final static int MIN_VALUE = 0;

    @Column(nullable = false)
    private int value;

    protected Distance() {}

    public Distance(Integer value) {
        validateValue(value);

        this.value = value;
    }

    public Distance addThenReturnResult(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    public void subtract(Distance distance) {
        if (this.value - distance.value < MIN_VALUE) {
            throw new IllegalArgumentException("거리 계산 결과가 0 보다 작을 수 없습니다.");
        }

        this.value -= distance.value;
    }

    public Distance subtractThenReturnResult(Distance distance) {
        Distance result = new Distance(this.value);
        result.subtract(distance);

        return result;
    }

    public int calculateDistanceRatio(Distance distance) {
        if (distance.value < MIN_VALUE) {
            throw new IllegalArgumentException("0보다 작은 거리로 비율을 계산할 수 없습니다.");
        }

        return this.value / distance.value + 1;
    }

    public boolean isLessThanOrEqualTo(Distance distance) {
        return this.value <= distance.value;
    }

    public boolean isLessThan(Distance distance) {
        return this.value < distance.value;
    }

    public Distance getMinimumDistance(Distance distance) {
        if (this.value > distance.value) {
            return distance;
        }
        return this;
    }

    public int getValue() {
        return this.value;
    }

    private void validateValue(Integer value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("거리는 0 보다 작을 수 없습니다.");
        }
    }
}
