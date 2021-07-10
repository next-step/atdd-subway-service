package nextstep.subway.path.util;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.DiscountInfo;

public class FareCalculator {
    private static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_FARE_PER_SECTION = 100;
    private static final int LONG_DISTANCE_FOR_EXTRA_FARE = 8;
    private static final int MIDDLE_DISTANCE_FOR_EXTRA_FARE = 5;
    private int fare;

    private FareCalculator(int fare) {
        this.fare = fare;
    }

    public static FareCalculator of(final Distance distance, final int surcharge, final DiscountInfo discountInfo) {
        int totalFare = DEFAULT_FARE;
        totalFare += calculateFareWithLongDistance(distance);
        totalFare += calculateFareWithMiddleDistance(distance);
        totalFare += surcharge;
        totalFare -= discountInfo.getDiscountFare();
        totalFare *= discountInfo.getDiscountRate();
        return new FareCalculator(totalFare);
    }

    private static int calculateFareWithLongDistance(final Distance totalDistance) {
        if (totalDistance.isLongDistance()) {
            return totalDistance.getLongDistance() / LONG_DISTANCE_FOR_EXTRA_FARE * ADDITIONAL_FARE_PER_SECTION;
        }
        return 0;
    }

    private static int calculateFareWithMiddleDistance(final Distance totalDistance) {
        if (totalDistance.hasMiddleDistance()) {
            return totalDistance.getMiddleDistance() / MIDDLE_DISTANCE_FOR_EXTRA_FARE * ADDITIONAL_FARE_PER_SECTION;
        }
        return 0;
    }

    public int getFare() {
        return this.fare;
    }
}
