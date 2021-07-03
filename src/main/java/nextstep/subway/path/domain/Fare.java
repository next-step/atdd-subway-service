package nextstep.subway.path.domain;

import java.util.List;

public class Fare {
    private static final double BASIC_FARE = 1250;
    private final double fare;

    public Fare(double fare) {
        this.fare = fare;
    }

    public static Fare of(List<FarePolicy> farePolicies) {
        double fare = BASIC_FARE;

        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.calculate(fare);
        }

        return new Fare(fare);
    }

    public double getFare() {
        return fare;
    }
}
