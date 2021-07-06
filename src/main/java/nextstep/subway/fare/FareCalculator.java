package nextstep.subway.fare;

import nextstep.subway.auth.domain.User;
import nextstep.subway.fare.domain.AgeBasedDiscount;
import nextstep.subway.fare.domain.DistanceBasedExtraCharge;
import nextstep.subway.fare.domain.Fare;

public class FareCalculator {

    public static final int BASE_FARE = 1250;

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
                .plus(DistanceBasedExtraCharge.calculate(distance))
                .plus(extraCharge);
        return AgeBasedDiscount.calculate(user.getAge(), fareBeforeAgeDiscount);
    }
}
