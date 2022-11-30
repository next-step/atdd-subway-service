package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;
    private final int value;

    protected Distance(){
        value = 0;
    }
    
    public Distance(final int value) {
        if (value <= MIN_DISTANCE) {
            throw new LineException(LineExceptionType.DISTANCE_MIN_ERROR);
        }
        this.value = value;
    }

    public Distance minusDistance(final int value) {
        if (this.value < value) {
            throw new LineException(LineExceptionType.DISTANCE_MINUS_ERROR);
        }
        return new Distance(this.value - value);
    }

    public Distance plusDistance(final int value) {
        return new Distance(this.value + value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
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
}
