package nextstep.subway.path.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;

public class BasicAgeDiscountPolicy implements AgeDiscountPolicy {

    @Override
    public int discount(int fare) {
        return Math.max(fare, BASIC_FARE);
    }

}
