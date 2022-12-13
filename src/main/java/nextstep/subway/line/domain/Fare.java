package nextstep.subway.line.domain;

import nextstep.subway.line.message.FareMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    public static final int ZERO = 0;

    @Column(nullable = false)
    private final Integer fare;

    protected Fare() {
        this.fare = null;
    }

    private Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    public static Fare zero() {
        return new Fare(ZERO);
    }

    public static Fare of(int fare) {
        return new Fare(fare);
    }

    private void validateFare(int fare) {
        if(fare < ZERO) {
            throw new IllegalArgumentException(FareMessage.FARE_SHOULD_BE_MORE_THAN_ZERO.message());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fare fare = (Fare) o;

        return this.fare.equals(fare.fare);
    }

    @Override
    public int hashCode() {
        return fare.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(fare);
    }
}
