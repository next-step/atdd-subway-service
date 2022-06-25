package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.FareType;

public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int fare) {
        return atLeastBasicFare(fare, FareType.BASIC.getFare());
    }
}
