package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 1;
    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new SubwayException(SubwayErrorCode.INVALID_DISTANCE);
        }
    }

    public Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    public Distance subtract(Distance distance) {
        if (this.value <= distance.value) {
            throw new SubwayException(SubwayErrorCode.NOT_LOWER_THAN_ORIGINAL_DISTANCE);
        }
        return new Distance(this.value - distance.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distance)) {
            return false;
        }
        Distance distance1 = (Distance)o;
        return value == distance1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
