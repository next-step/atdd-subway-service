package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;

public class FarePolicy {

    private static final int BASE_FARE = 1250;

    private FarePolicy() {
    }

    public static Fare of(LoginMember loginMember, Path path) {
        Fare maxExtraFare = path.getSections().getMaxExtraFare();
        Fare distanceExtraFare = calculatorExtraFareBy(path.getDistance().value());
        return calculateAgeDiscount(
                loginMember,
                Fare.from(BASE_FARE)
                        .plus(maxExtraFare)
                        .plus(distanceExtraFare)
        );
    }

    private static Fare calculateAgeDiscount(LoginMember loginMember, Fare fare) {
        if (loginMember.isGuest()) {
            return fare;
        }

        return AgeDiscountFarePolicy.valueOf(loginMember.getAge(), fare);
    }

    private static Fare calculatorExtraFareBy(int distance) {
        return DistanceFarePolicy.valueOf(distance);
    }
}
