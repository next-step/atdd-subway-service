package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public class AdultDiscountPolicy implements AgeDiscountPolicy {

    @Override
    public Fare calculateFare(Fare fare) {
        return fare;
    }
}
