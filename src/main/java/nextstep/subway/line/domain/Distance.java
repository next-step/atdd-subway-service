package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    public static final String CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE = "기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없습니다.";

    @Column
    private final int value;

    public Distance() {
        value = 0;
    }

    public Distance(int value) {
        this.value = value;
    }

    protected Distance minus(Distance distance) {
        if (isShortEqualThan(distance)) {
            throw new IllegalArgumentException(CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE);
        }
        return new Distance(value - distance.getValue());
    }

    private boolean isShortEqualThan(Distance distance) {
        return value <= distance.getValue();
    }

    protected Distance plus(Distance distance) {
        return new Distance(value + distance.getValue());
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
