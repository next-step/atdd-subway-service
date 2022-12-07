package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

import static nextstep.subway.line.domain.Distance.DISTANCE_UNIT_LEVEL1;
import static nextstep.subway.line.domain.Distance.DISTANCE_UNIT_LEVEL2;
import static nextstep.subway.path.domain.Fare.*;

public class FareCalculator {

    public static Fare calculateAdditionalFare(List<Section> sections, List<Station> stations, Distance distance) {
        int additionalFareOfLine = checkAdditionalFareOfLine(sections, stations);
        return new Fare(BASIC_FARE + additionalFareOfLine + calculateAdditionalFareOfDistance(distance));
    }

    private static int checkAdditionalFareOfLine(final List<Section> sections, final List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.containsAll(section.stations()))
                .map(section -> section.getLine().getAdditionalFare())
                .max(Integer::compareTo)
                .orElse(0);
    }

    private static int calculateAdditionalFareOfDistance(final Distance distance) {
        if (distance.isBiggerThen(ADDITIONAL_FARE_DISTANCE_LEVEL2)) {
            return calculateOverFare(distance.minus(ADDITIONAL_FARE_DISTANCE_LEVEL2), DISTANCE_UNIT_LEVEL2);
        }

        if (distance.isBiggerThen(ADDITIONAL_FARE_DISTANCE_LEVEL1)) {
            return calculateOverFare(distance.minus(ADDITIONAL_FARE_DISTANCE_LEVEL1), DISTANCE_UNIT_LEVEL1);
        }

        return 0;
    }

    private static int calculateOverFare(double distance, int distanceUnit) {
        return (int) ((Math.floor((distance - 1) / distanceUnit) + 1) * ADDITIONAL_FARE_UNIT);
    }

    public static Fare applyDiscountFare(final Fare fare, final Age age) {
        if (age.isChild()) {
            fare.applyDiscountFare(DISCOUNT_RATE_CHILD);
            return fare;
        }

        if (age.isTeen()) {
            fare.applyDiscountFare(DISCOUNT_RATE_TEEN);
            return fare;
        }

        return fare;
    }
}
