package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Fare {
    @Column(nullable = false)
    private BigDecimal fare;

    protected Fare() {}

    private Fare(int fare) {
        validateRange(fare);
        this.fare = BigDecimal.valueOf(fare);
    }

    private void validateRange(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FARE_RANGE.getMessage());
        }
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public Fare plus(Fare maxAddedFare) {
        return new Fare(this.fare.add(maxAddedFare.value()).intValue());
    }

    public BigDecimal value() {
        return fare;
    }
}
