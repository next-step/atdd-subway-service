package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    public static final String DISTANCE_CANNOT_BE_NEGATIVE = "구간의 거리는 음수일 수 없습니다.";
    public static final String CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE = "기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없습니다.";

    @Column(name = "distance")
    private final int value;

    public Distance() {
        value = 0;
    }

    private Distance(int value) {
        validationNegativeNumber(value);
        this.value = value;
    }

    public static Distance valueOf(int value) {
        return new Distance(value);
    }

    private void validationNegativeNumber(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(DISTANCE_CANNOT_BE_NEGATIVE);
        }
    }

    protected Distance minus(int distance) {
        return minus(new Distance(distance));
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

    protected Distance plus(int distance) {
        return plus(new Distance(distance));
    }

    protected Distance plus(Distance distance) {
        return new Distance(value + distance.getValue());
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
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
