package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;

public class TeenagersDiscountPolicy implements DiscountPolicy {
    private static final int BASIC_FARE = 1250;
    private static final int DEDUCTION_FARE = 350;
    @Override
    public Fare discount(int fare) {
        return Fare.from(Math.max(calculateFare(fare), calculateFare(BASIC_FARE)));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - DEDUCTION_FARE) * 0.8);
    }
}