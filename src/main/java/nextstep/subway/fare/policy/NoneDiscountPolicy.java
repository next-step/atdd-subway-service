package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;

public class NoneDiscountPolicy implements DiscountPolicy {
    private static final int BASIC_FARE = 1250;
    @Override
    public Fare discount(int fare) {
        return Fare.from(Math.max(fare, BASIC_FARE));
    }
}
