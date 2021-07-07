package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
    OVER_FIFTY_FARE(50, Integer.MAX_VALUE, 100, 8),
    OVER_TEN_FARE(10, 50, 100, 5),
    DEFAULT_FARE(0, 10, 0, 1);

    private final int minDistance;
    private final int maxDistance;
    private final int amountOfPer;
    private final int distanceOfPer;

    DistanceFarePolicy(int minDistance, int maxDistance, int amountOfPer, int distanceOfPer) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.amountOfPer = amountOfPer;
        this.distanceOfPer = distanceOfPer;
    }

    public static int calculate(int distance) {
        int sumOfFare = 0;
        while (distance > 0) {
            DistanceFarePolicy farePolicy = find(distance);
            sumOfFare += farePolicy.calculateOverFare(distance - farePolicy.minDistance);
            distance = farePolicy.minDistance;
        }
        return sumOfFare;
    }

    static DistanceFarePolicy find(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(it -> it.isTarget(distance))
                .findFirst()
                .orElse(DEFAULT_FARE);
    }

    private boolean isTarget(int distance) {
        return distance > minDistance && distance <= maxDistance;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / distanceOfPer) + 1) * amountOfPer);
    }
}
