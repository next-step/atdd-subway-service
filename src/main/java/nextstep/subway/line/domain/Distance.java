package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;

    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        this.value = value;
        validateDistance();
    }

    private void validateDistance() {
        if (value <= MIN_DISTANCE) {
            throw new IllegalArgumentException("구간의 길이는 0보다 커야합니다.");
        }
    }
}
