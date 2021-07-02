package nextstep.subway.path.additionalfarepolicy.distanceparepolicy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;

public enum OverFareByDistance {
    DISTANCE_10_KM(10, 50, 5, 100),
    DISTANCE_50_KM(50, Integer.MAX_VALUE, 8, 100);

    public static final int DEFAULT_USE_FARE_AMOUNT = 1250;

//    private final int overDistance;
    private final int startDistance;
    private final int endDistance;
    private final int addPercent;
    private final int overFare;

    OverFareByDistance(int startDistance, int endDistance, int addPercent, int overFare) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.addPercent = addPercent;
        this.overFare = overFare;
    }

//    OverFareByDistance(int overDistance, int addPercent, int overFare) {
//        this.overDistance = overDistance;
//        this.addPercent = addPercent;
//        this.overFare = overFare;
//    }

    public static Fare calculate(Distance distance) {
        int overAmount = DEFAULT_USE_FARE_AMOUNT;
        for (OverFareByDistance overFareByDistance : values()) {
            overAmount += overFareByDistance.calculateOverFare(distance);
        }
        return new Fare(overAmount);
    }

    private int calculateOverFare(Distance distance) {
        int distanceToInt = toDistanceToInt(distance);
        if (isOver(distance, startDistance)) {
            return ((distanceToInt - startDistance - 1) / addPercent + 1) * overFare;
        }
        return 0;
    }

    private int toDistanceToInt(Distance distance) {
        return (Math.min(distance.toInt(), endDistance));
    }

    private static boolean isOver(Distance distance, int overDistance) {
        return distance.isOver(overDistance);
    }
}
