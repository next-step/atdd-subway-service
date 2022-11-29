package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public class Fare {
    private int value;

    public Fare(Lines lines, int distance, int age) {
        int fare = lines.maxExtraFare().get() + FareDistance.calculate(distance);
        this.value =  fare - DiscountAge.calculate(age, fare);
    }

    public int get() {
        return value;
    }
}
