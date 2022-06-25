package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.AgeType;
import nextstep.subway.fare.domain.FareType;

public class TeenagersDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int fare) {
        return atLeastBasicFare(calculateFare(fare), calculateFare(FareType.BASIC.getFare()));
    }

    private int calculateFare(int fare) {
        return (int) ((fare - FareType.DEDUCTION.getFare()) * (1 - AgeType.TEENAGERS.getDiscountRate()));
    }
}