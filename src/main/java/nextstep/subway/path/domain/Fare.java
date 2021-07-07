package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fare.FareCalculator;

public class Fare {
    private int result;

    public Fare acceptPolicy(FareCalculator fareCalculator) {
        this.result = fareCalculator.calculate(this);
        return this;
    }

    public int getResult() {
        return result;
    }
}
