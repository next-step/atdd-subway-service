package nextstep.subway.path.application.fare.policy;

import java.util.Arrays;

public enum DistanceOverFarePolicy {
    BASIC_DISTANCE_COST(0, 10, 0, 0, null),
    MIDDLE_DISTANCE_COST(10, 50, 100, 5, BASIC_DISTANCE_COST),
    FAR_DISTANCE_COST(50, Integer.MAX_VALUE, 100, 8, MIDDLE_DISTANCE_COST);

    private static final int BASIC_FARE = 1_250;

    private final int minDistance;
    private final int maxDistance;
    private final int extraChargePerUnit;
    private final int unitOfDistance;
    private final DistanceOverFarePolicy nextPolicy;

    DistanceOverFarePolicy(int minDistance, int maxDistance, int extraChargePerUnit, int unitOfDistance,
                           DistanceOverFarePolicy nextPolicy) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.extraChargePerUnit = extraChargePerUnit;
        this.unitOfDistance = unitOfDistance;
        this.nextPolicy = nextPolicy;
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

    private int calculateOverFare() {
        return calculateOverFare(maxDistance - minDistance);
    }

    public int calculateFare(int distance) {
        int fare = BASIC_FARE;
        fare += calculateOverFare(distance - this.minDistance);

        DistanceOverFarePolicy nextFarePolicy = this.nextPolicy;
        while(nextFarePolicy != null) {
            fare += nextFarePolicy.calculateOverFare();
            nextFarePolicy = nextFarePolicy.nextPolicy;
        }

        return fare;
    }
}
