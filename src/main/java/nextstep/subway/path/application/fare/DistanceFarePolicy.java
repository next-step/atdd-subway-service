package nextstep.subway.path.application.fare;

import java.util.Arrays;

public enum DistanceFarePolicy {
    BASIC_DISTANCE_COST(0, 10, 0, 0),
    MIDDLE_DISTANCE_COST(10, 50, 100, 5),
    FAR_DISTANCE_COST(50, Integer.MAX_VALUE, 100, 8);

    private final int min_distance;
    private final int max_distance;
    private final int extraChargePerUnit;
    private final int unitOfDistance;

    DistanceFarePolicy(int min_distance, int max_distance, int extraChargePerUnit, int unitOfDistance) {
        this.min_distance = min_distance;
        this.max_distance = max_distance;
        this.extraChargePerUnit = extraChargePerUnit;
        this.unitOfDistance = unitOfDistance;
    }

    public static DistanceFarePolicy valueOf(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(filteredRank -> filteredRank.valid(distance))
                .findFirst()
                .orElse(BASIC_DISTANCE_COST);
    }

    private boolean valid(int distance) {
        return distance > min_distance && distance <= max_distance;
    }

    public int calculateOverFare(int distance) {
        if (unitOfDistance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unitOfDistance ) + 1) * extraChargePerUnit);
    }
}
