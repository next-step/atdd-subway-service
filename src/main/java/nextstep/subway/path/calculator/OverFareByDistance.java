package nextstep.subway.path.calculator;

import nextstep.subway.line.domain.Distance;

public enum OverFareByDistance {
    DISTANCE_10_KM(10, 5, 100),
    DISTANCE_50_KM(50, 8, 100);

    public static final int DEFAULT_USE_FARE_AMOUNT = 1250;

    private final int overDistance;
    private final int addPercent;
    private final int overFare;

    OverFareByDistance(int overDistance, int addPercent, int overFare) {
        this.overDistance = overDistance;
        this.addPercent = addPercent;
        this.overFare = overFare;
    }

    public static int calculate(Distance distance) {
        int overAmount = DEFAULT_USE_FARE_AMOUNT;
        for (OverFareByDistance overFareByDistance : values()) {
            overAmount += overFareByDistance.calculateOverFare(distance);
        }
        return overAmount;
    }

    private int calculateOverFare(Distance distance) {
        int distanceToInt = toDistanceToInt(distance);
        if (isOver(distance, this.overDistance)) {
            return (int) ((Math.ceil((distanceToInt - this.overDistance - 1) / this.addPercent) + 1) * this.overFare);
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
