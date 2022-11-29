package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public class Fare {
    private int value;

    public Fare(Lines lines, int distance) {
        this.value = lines.maxExtraFare().get() + FareDistance.calculate(distance);
    }

    public int get() {
        return value;
    }
}
