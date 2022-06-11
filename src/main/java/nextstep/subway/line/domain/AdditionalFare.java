package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFare {

    @Column(name = "additional_fare", nullable = false)
    private int value;

    protected AdditionalFare() {}

    private AdditionalFare(int fare) {
        this.value = fare;
    }

    public static AdditionalFare from(int fare) {
        validateAdditionalFare(fare);
        return new AdditionalFare(fare);
    }

    public int getValue() {
        return this.value;
    }

    private static void validateAdditionalFare(int fare) {
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
        AdditionalFare additionalFare = (AdditionalFare) o;
        return value == additionalFare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
