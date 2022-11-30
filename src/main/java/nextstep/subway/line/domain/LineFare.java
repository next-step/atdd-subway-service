package nextstep.subway.line.domain;

import nextstep.subway.exception.NegativeOverFareException;
import nextstep.subway.message.ExceptionMessage;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LineFare implements Comparable<LineFare> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineFare lineFare = (LineFare) o;
        return fare == lineFare.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public int compareTo(LineFare o) {
        return Integer.compare(this.fare, o.fare);
    }
}
