package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public class AdditionalFareOfLinePolicy implements FareCalculator {
    @Override
    public int calculate(Fare fare) {
        return fare.getResult() + fare.getMaxAdditionalFare();
    }
}
