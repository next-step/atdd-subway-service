package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public class InfantDiscountPolicy implements AgeDiscountPolicy {

    private static final double DISCOUNT_RATE = 1.0;

    @Override
    public Fare calculateFare(final Fare fare) {
        int discount = (int) (fare.getFare() * DISCOUNT_RATE);
        return fare.subtract(discount);
    }
}
