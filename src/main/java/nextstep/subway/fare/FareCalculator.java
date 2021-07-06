package nextstep.subway.fare;

import nextstep.subway.auth.domain.User;
import nextstep.subway.fare.domain.AgeBasedDiscount;
import nextstep.subway.fare.domain.DistanceBasedExtraCharge;
import nextstep.subway.fare.domain.Fare;

import static nextstep.subway.fare.domain.Fare.BASE_FARE;

public class FareCalculator {

    private User user;
    private int distance;
    private Fare extraCharge;

    public FareCalculator(User user, int distance, Fare extraCharge) {
        this.user = user;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public Fare calculate() {
        Fare fareBeforeAgeDiscount = new Fare(BASE_FARE)
                .plus(DistanceBasedExtraCharge.newCalculate(distance))
                .plus(extraCharge);
        return AgeBasedDiscount.newCalculate(user.getAge(), fareBeforeAgeDiscount);
    }
}
