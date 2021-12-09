package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public class DistanceFareCalculator {
    public static final int BASIC_FARE = 1250;
    private static final int LARGE_DIVIDE_UNIT = 8;
    private static final int MEDIUM_DIVIDE_UNIT = 5;
    private static final int EXTRA_FARE = 100;
    private static final String ILLEGAL_DISTANCE_ERROR_MESSAGE = "잘못된 거리입니다.";

    private DistanceFareCalculator() {
    }

    public static int calculateOverFare(Distance distance) {
        if (distance.isShortRange()) {
            return BASIC_FARE;
        }

        if (distance.isMediumRange()) {
            final int mediumFare = calculateMediumRangeFare(distance);
            return BASIC_FARE + mediumFare;
        }

        if (distance.isLargeRange()) {
            final int maxMediumRangeFare = calculateMediumRangeFare(Distance.ofMaxMedium());
            final int largeRangeFare = calculateLargeRangeFare(distance);
            return BASIC_FARE + maxMediumRangeFare + largeRangeFare;
        }

        throw new IllegalArgumentException(ILLEGAL_DISTANCE_ERROR_MESSAGE);
    }

    private static int calculateLargeRangeFare(Distance distance) {
        final int largeRangeDistance = distance.minus(Distance.MEDIUM_DISTANCE).intValue();
        return calculateFareByDistanceAndUnit(largeRangeDistance, LARGE_DIVIDE_UNIT);
    }

    private static int calculateMediumRangeFare(Distance distance) {
        final int mediumRangeDistance = distance.minus(Distance.SHORT_DISTANCE).intValue();
        return calculateFareByDistanceAndUnit(mediumRangeDistance, MEDIUM_DIVIDE_UNIT);
    }

    private static int calculateFareByDistanceAndUnit(int distance, int unit) {
        return (int) ((Math.floor((double) (distance - 1) / unit) + 1) * EXTRA_FARE);
    }
}
