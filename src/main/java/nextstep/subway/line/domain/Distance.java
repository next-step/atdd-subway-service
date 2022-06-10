package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;

@Embeddable
public final class Distance {

    @Column(name = "distance")
    private final int value;

    protected Distance() {
        this.value = 0;
    }

    public Distance(int value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(int value) {
        if (value <= 0) {
            throw new BadRequestException(ExceptionType.MUST_BE_AT_LEAST_LENGTH_ONE);
        }
    }

    public Distance plus(Distance target) {
        return new Distance(this.value + target.getValue());
    }

    public Distance minus(Distance target) {
        validateDistance(target);
        return new Distance(this.value - target.getValue());
    }

    private void validateDistance(Distance target) {
        if (target.getValue() >= value) {
            throw new BadRequestException(ExceptionType.IS_NOT_OVER_ORIGIN_DISTANCE);
        }
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
        return getValue() == distance.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
