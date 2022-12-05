package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineExceptionCode;
import nextstep.subway.utils.NumberUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {
    @Column
    private int fare = 0;

    protected Fare() {
    }

    public Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    private void validateFare(int fare) {
        if(NumberUtil.isNotPositiveNumber(fare)) {
            throw new IllegalArgumentException(
                    LineExceptionCode.DO_NOT_ALLOW_NEGATIVE_NUMBER_FARE.getMessage());
        }
    }

    @Override
    public int compareTo(Fare o) {
        return Integer.compare(this.fare, o.fare);
    }

    public int getFare() {
        return fare;
    }
}
