package nextstep.subway.path.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.Fare.DEDUCTION_FARE;

public class KidsAgeDiscountPolicy implements AgeDiscountPolicy {
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    @Override
    public int discount(int fare) {
        return (Math.max(calculateFare(fare), calculateFare(BASIC_FARE)));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * CHILD_DISCOUNT_RATE);
    }

}
