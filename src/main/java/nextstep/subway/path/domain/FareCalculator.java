package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

public class FareCalculator {
    private static final int BASE_FARE = 1250;

    public int calculate(Distance distance, List<Line> lines) {
        int totalFare = BASE_FARE;

        int distanceExcessFare = DistanceFarePolicy.calculateTotalExcessFare(distance);
        int lineExcessFare = LineFarePolicy.calculateExcessFare(lines);

        return totalFare + distanceExcessFare + lineExcessFare;
    }
}
