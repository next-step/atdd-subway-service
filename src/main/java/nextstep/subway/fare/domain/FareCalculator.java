package nextstep.subway.fare.domain;

import java.util.Set;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;

public class FareCalculator {
    public static final int MIN_NUM = 0;

    public static Fare calculateFare(Set<Line> lines, Path path) {
        Distance distance = path.distance();
        Fare lineFare = maxAdditionalFareByLines(lines);
        Fare distanceFare = DistanceFarePolicy.findDistanceFarePolicyByDistance(distance).calculateFare(distance);
        lineFare.add(distanceFare);
        return lineFare;
    }

    private static Fare maxAdditionalFareByLines(Set<Line> lines) {
        return lines.stream()
                .map(Line::additionalFare)
                .max(Fare::compareTo)
                .orElse(Fare.valueOf(MIN_NUM));
    }

}
