package nextstep.subway.path.calculator;

import nextstep.subway.line.domain.Distance;

public enum OverFare {
    DISTANCE_10_KM(10, 5),
    DISTANCE_50_KM(50, 8);

    private static final int DEFAULT_USE_FARE_AMOUNT = 1250;

    int overDistance;
    int discountPer;

    OverFare(int overDistance, int discountPer) {
        this.overDistance = overDistance;
        this.discountPer = discountPer;
    }

    public static int calculate(Distance distance) {
        int overAmount = DEFAULT_USE_FARE_AMOUNT;
        for (OverFare overFare : values()) {
            overAmount += overFare.calculateOverFare(distance);
        }
        return overAmount;
    }

    private int calculateOverFare(Distance distance) {
        int distanceToInt = toDistanceToInt(distance);
        if (isOver(distance, this.overDistance)) {
            return (int) ((Math.ceil((distanceToInt - this.overDistance - 1) / this.discountPer) + 1) * 100);
        }
        return 0;
    }

    private int toDistanceToInt(Distance distance) {
        if (this.equals(DISTANCE_10_KM)) {
            return (Math.min(distance.toInt(), DISTANCE_50_KM.overDistance));
        }
        return distance.toInt();
    }

    private static boolean isOver(Distance distance, int overDistance) {
        return distance.isOver(overDistance);
    }
}
