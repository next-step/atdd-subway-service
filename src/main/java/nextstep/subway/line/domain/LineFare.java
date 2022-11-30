package nextstep.subway.line.domain;

import nextstep.subway.exception.NegativeOverFareException;
import nextstep.subway.message.ExceptionMessage;

import javax.persistence.Embeddable;

@Embeddable
public class LineFare {
    private static final int MIN_FARE = 0;

    private int fare;

    protected LineFare() {
    }

    private LineFare(int fare) {
        this.fare = fare;
    }

    public static LineFare zero() {
        return new LineFare(0);
    }

    public static LineFare from(int fare) {
        checkFareNotNegative(fare);
        return new LineFare(fare);
    }

    private static void checkFareNotNegative(int fare) {
        if (fare < MIN_FARE) {
            throw new NegativeOverFareException(ExceptionMessage.INVALID_OVER_FARE);
        }
    }
}
