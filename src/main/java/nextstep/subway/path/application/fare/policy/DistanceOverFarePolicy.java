package nextstep.subway.path.application.fare.policy;

import java.util.Arrays;

public enum DistanceOverFarePolicy {
    BASIC_DISTANCE_COST(0, 10, 0, 0),
    MIDDLE_DISTANCE_COST(10, 50, 100, 5),
    FAR_DISTANCE_COST(50, Integer.MAX_VALUE, 100, 8);

    private static final int BASIC_FARE = 1_250;

    private final int minDistance;
    private final int maxDistance;
    private final int extraChargePerUnit;
    private final int unitOfDistance;

    DistanceOverFarePolicy(int minDistance, int maxDistance, int extraChargePerUnit, int unitOfDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.extraChargePerUnit = extraChargePerUnit;
        this.unitOfDistance = unitOfDistance;
    }

    public static DistanceOverFarePolicy valueOf(int distance) {
        return Arrays.stream(DistanceOverFarePolicy.values())
                .filter(filteredRank -> filteredRank.valid(distance))
                .findFirst()
                .orElse(BASIC_DISTANCE_COST);
    }

    private boolean valid(int distance) {
        return distance > minDistance && distance <= maxDistance;
    }

    public int calculateOverFare(int distance) {
        if (unitOfDistance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unitOfDistance ) + 1) * extraChargePerUnit);
    }

    public static int calculateFare(int distance) {
        int fare = BASIC_FARE;
        int remainDistance = distance;
        do {
            DistanceOverFarePolicy distanceOverFarePolicy = valueOf(remainDistance);
            fare += distanceOverFarePolicy.calculateOverFare(remainDistance - distanceOverFarePolicy.minDistance);
            remainDistance = distanceOverFarePolicy.minDistance;
        } while (remainDistance > 0);

        return fare;
    }
}
