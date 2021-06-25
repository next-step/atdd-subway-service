package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    public static final String INVALID_DISTANCE_EXCEPTION_MESSAGE = "구간 거리는 0보다 커야합니다.";
    public static final String BIGGER_THAN_DISTANCE_EXCEPTION_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요.";

    @Column(name = "distance")
    private final int value;

    protected Distance() {
        value = 0;
    }

    private Distance(int distance) {
        validate(distance);
        this.value = distance;
    }

    public static Distance valueOf(int distance) {
        return new Distance(distance);
    }

    private void validate(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(INVALID_DISTANCE_EXCEPTION_MESSAGE);
        }
    }

    public int getValue() {
        return value;
    }

    public Distance minus(Distance distanceToMinus) {
        if (!isAvailableMinus(distanceToMinus)) {
            throw new IllegalArgumentException(BIGGER_THAN_DISTANCE_EXCEPTION_MESSAGE);
        }
        return Distance.valueOf(value - distanceToMinus.getValue());
    }

    private boolean isAvailableMinus(Distance distanceToMinus) {
        return value > distanceToMinus.getValue();
    }

    public Distance plus(Distance distanceToPlus) {
        return Distance.valueOf(value + distanceToPlus.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return value == distance1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
