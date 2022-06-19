package nextstep.subway.line.domain;

import nextstep.subway.line.consts.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFare {
    private static int MINIMUM_ADDITIONAL_FARE = 0;

    @Column(name = "additional_fare", nullable = false)
    private int value;

    protected AdditionalFare() {
    }

    public AdditionalFare(int value) {
        validateAdditionalFare(value);
        this.value = value;
    }

    private void validateAdditionalFare(int value) {
        if (value < MINIMUM_ADDITIONAL_FARE) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.ERROR_ADDITIONAL_FARE_TOO_SMALL, MINIMUM_ADDITIONAL_FARE, value)
            );
        }
    }

    public static AdditionalFare from(int additionalFare) {
        return new AdditionalFare(additionalFare);
    }

    public int getValue() {
        return value;
    }
}
