package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public class YouthDiscountPolicy implements AgeDiscountPolicy {

    private static final int DEDUCT_WITH = 350;
    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public Fare calculateFare(Fare fare) {
        Fare discount = fare.subtract(DEDUCT_WITH).multiply(DISCOUNT_RATE);
        return fare.subtract(DEDUCT_WITH).subtract(discount.getFare());
    }
}
