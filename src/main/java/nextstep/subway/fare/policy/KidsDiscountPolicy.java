package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.FareType;

public class KidsDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int fare) {
        return Math.max(calculateFare(fare), calculateFare(FareType.BASIC.getFare()));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - FareType.DEDUCTION.getFare()) * 0.5);
    }
}
