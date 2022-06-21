package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;
    private int value;

    protected Distance(){}
    
    public Distance(final int value) {
        if (value <= MIN_DISTANCE) {
            throw new LineException(LineExceptionType.DISTANCE_MIN_ERROR);
        }
        this.value = value;
    }

    public void minusDistance(final int value) {
        if (this.value < value) {
            throw new LineException(LineExceptionType.DISTANCE_MINUS_ERROR);
        }
        this.value = this.value - value;
    }

    public void plusDistance(final int value) {
        this.value = this.value + value;
    }

    public int getValue() {
        return value;
    }
}
