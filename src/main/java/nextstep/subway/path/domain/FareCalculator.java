package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE_UNIT = 100;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL1 = 10;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL2 = 50;
    public static final int DEDUCTION_AMOUNT = 350;
    public static final double CHILD_AGE_MIN = 6;
    public static final double TEEN_AGE_MIN = 13;
    public static final double TEEN_AGE_MAX = 18;
    public static final double DISCOUNT_RATE_CHILD = 0.5;
    public static final double DISCOUNT_RATE_TEEN = 0.2;
    public static final int DISTANCE_UNIT_LEVEL1 = 5;
    public static final int DISTANCE_UNIT_LEVEL2 = 8;

    public static int calculateAdditionalFare(List<Section> sections, List<Station> stations, Distance distance) {
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
        return age >= CHILD_AGE_MIN && age < TEEN_AGE_MIN;
    }

    private static boolean isTeen(int age) {
        return age >= TEEN_AGE_MIN && age <= TEEN_AGE_MAX;
    }

    private static int applyChildDiscountFare(int fare) {
        return (int) (fare - ((fare - DEDUCTION_AMOUNT) * DISCOUNT_RATE_CHILD));
    }

    private static int applyTeenDiscountFare(int fare) {
        return (int) (fare - ((fare - DEDUCTION_AMOUNT) * DISCOUNT_RATE_TEEN));
    }
}
