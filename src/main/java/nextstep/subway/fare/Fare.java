package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static nextstep.subway.fare.FareConstants.*;

@Embeddable
public class Fare {
    @Column(nullable = false)
    private long fare;

    public Fare() {
        this.fare = ZERO_FARE;
    }

    private Fare(long fare) {
        checkValidation(fare);
        this.fare = fare;
    }

    public static Fare from() {
        return new Fare();
    }

    public static Fare from(long fare) {
        return new Fare(fare);
    }

    public static Fare fromBaseFare() {
        return new Fare(BASE_FARE);
    }

    public static Fare fromBaseFare(long addFare) {
        return new Fare(BASE_FARE + addFare);
    }

    public long currentFare() {
        return this.fare;
    }

    public long currentFare(int distance, LoginMember member) {
        WooTechSubwayFareCalculator wooTechSubwayFareCalculator = new WooTechSubwayFareCalculator(this.fare);
        return wooTechSubwayFareCalculator.fareCalculate(distance, member);
    }

    public long findFare() {
        return this.fare;
    }

    private void checkValidation(long fare) {
        if (isNegative(fare)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FARE_FORMAT.getErrorMessage());
        }
    }

    private boolean isNegative(long fare) {
        return fare < 0;
    }
}
