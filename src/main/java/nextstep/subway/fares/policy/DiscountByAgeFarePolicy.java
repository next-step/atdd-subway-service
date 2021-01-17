package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;

public class DiscountByAgeFarePolicy implements FarePolicy {
    private static final int DEDUCT_FARE = 350;

    @Override
    public void calculateFare(Fare fare, FareContext fareContext) {
        LoginMember loginMember = fareContext.getLoginMember();
        if (!loginMember.isAnonymous()) {
            DiscountByAge discountRateByAge = DiscountByAge.getDiscountRateByAge(loginMember.getAge());
            discountFare(fare, discountRateByAge);
        }
    }

    private void discountFare(Fare fare, DiscountByAge discountRateByAge) {
        if (discountRateByAge != DiscountByAge.NONE) {
            fare.minus(DEDUCT_FARE);
            fare.discount(discountRateByAge.getRate());
        }
    }
}
