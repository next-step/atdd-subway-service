package nextstep.subway.fare.domain;

import java.util.Set;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;
import nextstep.subway.path.domain.Path;

public class FareCalculator {
    private static final int MIN_NUM = 0;

    public static Fare calculateFare(Path path, Age age) {
        Distance distance = path.distance();
        Fare fare = DistanceFarePolicy.findDistanceFarePolicyByDistance(distance).calculateFare(distance);
        fare.add(maxAdditionalFareByLines(path.lines()));
        AgeFarePolicy.findAgeFarePolicyByAgeGroup(AgeGroup.findAgeGroupByAge(age)).discountFare(fare);
        return fare;
    }

    private static Fare maxAdditionalFareByLines(Set<Line> linesOfPath) {
        return linesOfPath.stream()
                .map(Line::additionalFare)
                .max(Fare::compareTo)
                .orElse(Fare.valueOf(MIN_NUM));
    }
}
