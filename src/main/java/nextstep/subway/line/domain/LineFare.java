package nextstep.subway.line.domain;

import nextstep.subway.exception.NegativeOverFareException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static nextstep.subway.utils.Message.INVALID_OVER_FARE;

@Embeddable
public class LineFare implements Comparable<LineFare> {
    private static final int MIN_FARE = 0;

    @Column
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
            throw new NegativeOverFareException(INVALID_OVER_FARE);
        }
    }

    public int getFare() {
        return fare;
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
