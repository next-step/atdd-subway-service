package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {
    private static final int MIN_FARE = 0;

    @Column(nullable = false)
    private int fare;

    protected Fare() {}

    private Fare(int fare) {
        validateRange(fare);
        this.fare = fare;
    }

    private void validateRange(int fare) {
        if (fare < MIN_FARE) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FARE_RANGE.getMessage());
        }
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public Fare plus(Fare maxAddedFare) {
        return new Fare(this.fare + maxAddedFare.value());
    }

    public int value() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
