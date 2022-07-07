package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public class FareCalculator {
    private static final int BASE_FARE = 1250;

    public int calculate(Distance distance) {
        int totalFare = BASE_FARE;

        int distanceExcessFare = DistanceFarePolicy.calculateTotalExcessFare(distance);

        return totalFare + distanceExcessFare;
    }
}
