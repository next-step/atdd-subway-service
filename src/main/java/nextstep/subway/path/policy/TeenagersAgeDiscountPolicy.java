package nextstep.subway.path.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.Fare.DEDUCTION_FARE;

public class TeenagersAgeDiscountPolicy implements AgeDiscountPolicy {
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    @Override
    public int discount(int fare) {
        return Math.max(calculateFare(fare), calculateFare(BASIC_FARE));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATE);
    }

}
