package nextstep.subway.fare.domain;

import nextstep.subway.fare.message.FareMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Arrays;

@Embeddable
public class Fare implements Comparable<Fare> {

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

    public static Fare sum(Fare... others) {
        int sumOtherFares = Arrays.stream(others)
                .mapToInt(other -> other.fare)
                .sum();
        return new Fare(sumOtherFares);
    }

    private void validateFare(int fare) {
        if(fare < ZERO) {
            throw new IllegalArgumentException(FareMessage.FARE_SHOULD_BE_MORE_THAN_ZERO.message());
        }
    }

    public int value() {
        return this.fare;
    }

    public Fare plus(Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public Fare minus(Fare other) {
        return new Fare(this.fare - other.fare);
    }

    @Override
    public int compareTo(Fare other) {
        return this.fare.compareTo(other.fare);
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

    public Fare discountByPercent(int percent) {
        return new Fare(this.fare * (100 - percent) / 100);
    }
}
