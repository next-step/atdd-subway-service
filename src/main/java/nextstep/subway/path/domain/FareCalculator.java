package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL1 = 10;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL2 = 50;
    public static final int DISTANCE_UNIT_LEVEL1 = 5;
    public static final int DISTANCE_UNIT_LEVEL2 = 8;

    public static int calculateAdditionalFare(List<Section> sections, List<Station> stations, double distance) {
        int additionalFareOfLine = checkAdditionalFareOfLine(sections, stations);
        return BASIC_FARE + additionalFareOfLine + calculateAdditionalFareOfDistance(distance);
    }

    private static int checkAdditionalFareOfLine(final List<Section> sections, final List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.containsAll(section.stations()))
                .map(section -> section.getLine().getAdditionalFare())
                .max(Integer::compareTo)
                .orElse(0);
    }

    private static int calculateAdditionalFareOfDistance(final double weight) {
        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL2) {
            return calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL2, DISTANCE_UNIT_LEVEL2);
        }

        if (weight > ADDITIONAL_FARE_DISTANCE_LEVEL1) {
            return calculateOverFare(weight - ADDITIONAL_FARE_DISTANCE_LEVEL1, DISTANCE_UNIT_LEVEL1);
        }

        return 0;
    }

    private static int calculateOverFare(double distance, int distanceUnit) {
        return (int) ((Math.floor((distance - 1) / distanceUnit) + 1) * 100);
    }

    public static int applyDiscountFare(final int fare, final int age) {
        if (isChild(age)) {
            return applyChildDiscountFare(fare);
        }

        if (isTeen(age)) {
            return applyTeenDiscountFare(fare);
        }
        return fare;
    }

    private static boolean isChild(int age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeen(int age) {
        return age >= 13 && age < 19;
    }

    private static int applyChildDiscountFare(int fare) {
        return (int) (fare - ((fare - 350) * 0.5));
    }

    private static int applyTeenDiscountFare(int fare) {
        return (int) (fare - ((fare - 350) * 0.2));
    }
}
