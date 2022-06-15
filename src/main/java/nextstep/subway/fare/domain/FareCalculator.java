package nextstep.subway.fare.domain;

import java.util.Set;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.member.domain.Age;

public class FareCalculator {
    private static final int MIN_NUM = 0;

    public static Fare calculateFare(Set<Line> lines, Path path, Age age) {
        Distance distance = path.distance();
        Fare fare = DistanceFarePolicy.findDistanceFarePolicyByDistance(distance).calculateFare(distance);
        fare.add(maxAdditionalFareByLines(lines));
        AgeFarePolicy.findAgeFarePolicyByAge(age).discountFare(fare);
        return fare;
    }

    private static Fare maxAdditionalFareByLines(Set<Line> lines) {
        return lines.stream()
                .map(Line::additionalFare)
                .max(Fare::compareTo)
                .orElse(Fare.valueOf(MIN_NUM));
    }
}
