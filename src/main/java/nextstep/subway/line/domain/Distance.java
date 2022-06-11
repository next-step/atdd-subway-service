package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;

    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        validateDistance(value);
        this.value = value;
    }

    private void validateDistance(int value) {
        if (value <= MIN_DISTANCE) {
            throw new IllegalArgumentException("구간의 길이는 0보다 커야합니다.");
        }
    }

    public Distance minus(Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance plus(Distance distance) {
        return new Distance(value + distance.value);
    }

    public int value() {
        return value;
    }
}
