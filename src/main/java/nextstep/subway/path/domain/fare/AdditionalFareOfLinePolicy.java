package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public class AdditionalFareOfLinePolicy implements FareCalculator {

    private final int maxAddtionalFare;

    public AdditionalFareOfLinePolicy(final int maxAddtionalFare) {
        this.maxAddtionalFare = maxAddtionalFare;
    }

    @Override
    public int calculate(Fare fare) {
        return fare.getResult() + maxAddtionalFare;
    }
}
