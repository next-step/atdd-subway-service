package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;

public class FareCalculator {

    public static Fare applyDiscountFare(final Fare fare, final Age age) {
        AgePolicy agePolicy = Arrays.stream(AgePolicy.values())
                .filter(policy -> policy.isDiscountable(age))
                .findFirst()
                .orElse(AgePolicy.DEFAULT);

        if (agePolicy == AgePolicy.DEFAULT) {
            return fare;
        }

        return agePolicy.applyDiscountFare(fare);
    }

    public static Fare calculateAdditionalFare(List<Section> sections, List<Station> stations, Distance distance) {
        int additionalFareOfLine = checkAdditionalFareOfLine(sections, stations);
        return new Fare(BASIC_FARE + additionalFareOfLine + DiscountPolicy.calculateAdditionalFareOfDistance(distance));
    }

    private static int checkAdditionalFareOfLine(final List<Section> sections, final List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.containsAll(section.stations()))
                .map(section -> section.getLine().getAdditionalFare())
                .max(Integer::compareTo)
                .orElse(0);
    }
}
