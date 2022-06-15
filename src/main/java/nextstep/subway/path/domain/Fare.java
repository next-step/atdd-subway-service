package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public class Fare {
    private Set<Line> chargedLines;
    private int distance;

    public Fare(Path path) {
        this.chargedLines = path.getChargedLines();
        this.distance = path.getShortestDistance();
    }

    public int getTotalFare() {
        int totalFare = 0;
        totalFare += DistanceFarePolicy.calculateFare(distance);
        totalFare += chargedLines.stream().
                mapToInt(line -> line.getExtraCharge()).
                max().orElse(0);
        return totalFare;
    }
}
