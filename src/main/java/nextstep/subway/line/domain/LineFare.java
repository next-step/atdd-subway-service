package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.ErrorEnum;

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
