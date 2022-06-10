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
        this.value -= distance.value;
    }

    public boolean isLessThanOrEqualTo(Distance distance) {
        return this.value <= distance.value;
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
