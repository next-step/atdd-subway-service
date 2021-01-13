package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Path;

public class DiscountByAgeFarePolicy implements FarePolicy {
    private static final int DEDUCT_FARE = 350;

    @Override
    public void calculateFare(Fare fare, Path path, LoginMember loginMember) {
        if (!loginMember.isAnonymous()) {
            double discountRate = DiscountByAge.getDiscountRateByAge(loginMember.getAge());
            fare.minus(DEDUCT_FARE);
            fare.discount(discountRate);
        }
    }
}
