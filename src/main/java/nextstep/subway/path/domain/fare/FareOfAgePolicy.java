package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public class FareOfAgePolicy implements FareCalculator {

    @Override
    public int calculate(Fare fare) {
        return FareOfAgePolicyFactory.discount(fare.getAge(), fare.getResult());
    }
}
