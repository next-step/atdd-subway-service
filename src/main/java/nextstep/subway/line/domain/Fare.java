package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    @Column(name = "fare", nullable = false)
    private int value;

    protected Fare() {}

    private Fare(int fare) {
        this.value = fare;
    }

    public static Fare from(int fare) {
        validateFare(fare);
        return new Fare(fare);
    }

    public int getValue() {
        return this.value;
    }

    private static void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(LineExceptionType.LINE_FARE_IS_OVER_ZERO.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
