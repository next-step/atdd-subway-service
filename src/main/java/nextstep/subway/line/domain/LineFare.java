package nextstep.subway.line.domain;

import nextstep.subway.common.exception.ErrorEnum;

public class LineFare {
    private static final int MIN_FARE = 0;
    private int fare;

    protected LineFare() {
    }

    private LineFare(int fare) {
        this.fare = fare;
    }

    public static LineFare zero() {
        return new LineFare(MIN_FARE);
    }

    public static LineFare from(int fare) {
        checkMinLineFare(fare);
        return new LineFare(fare);
    }

    private static void checkMinLineFare(int fare) {
        if (fare < MIN_FARE) {
            throw new IllegalArgumentException(ErrorEnum.LINE_FARE_GREATER_ZERO.message());
        }
    }
}
