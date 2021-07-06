package nextstep.subway.station.domain;

import java.util.Arrays;

public enum DistanceBasedFarePolicy {

    DISTANCE_1(10, 50, 5, 100),
    DISTANCE_2(50, Integer.MAX_VALUE, 8, 100);

    private static final int ZERO_FARE = 0;
    private static final int DISTANCE_NONE = -1;

    private int minDistance;
    private int maxDistance;
    private int chargeUnit;
    private int overFare;

    DistanceBasedFarePolicy(int minDistance, int maxDistance, int chargeUnit, int overFare) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.chargeUnit = chargeUnit;
        this.overFare = overFare;
    }

    public static int overFare(int distance) {
        return Arrays.stream(values())
                .mapToInt(policy -> policy.calculateOverFare(distance))
                .sum();
    }

    private int calculateOverFare(int distance) {
        int distancePerUnit = calculateDistancePerUnit(distance);
        if (distancePerUnit == DISTANCE_NONE) {
            return ZERO_FARE;
        }
        return distancePerUnit / chargeUnit * overFare;
    }

    private int calculateDistancePerUnit(int distance) {
        if (distance < minDistance) {
            return DISTANCE_NONE;
        }
        return Math.min(distance, maxDistance) - minDistance;
    }
}
