package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.Fare.OVER_FEE;

public enum DistancePolicy {
    MINIMUM_BOUNDARY_DISTANCE(10),
    MAXIMUM_BOUNDARY_DISTANCE(50),
    ADDED_DISTANCE_UNDER_MAXIMUM_BOUNDARY(5),
    ADDED_DISTANCE_UPON_MAXIMUM_BOUNDARY(8),
    POD_DISTANCE(1);

    private final int distance;

    DistancePolicy(int distance) {
        this.distance = distance;
    }

    public static boolean isNotAddedFareDistance(int distance) {
        return distance < MINIMUM_BOUNDARY_DISTANCE.distance;
    }

    public static boolean isFirstAddedFareDistance(int distance) {
        return distance >= MINIMUM_BOUNDARY_DISTANCE.distance && distance < MAXIMUM_BOUNDARY_DISTANCE.distance;
    }

    int calculateOverFare(int pathDistance) {
        return (int) (calculateOverDistance(pathDistance) * OVER_FEE);
    }

    private double calculateOverDistance(int pathDistance) {
        return Math.ceil(
                (pathDistance - MINIMUM_BOUNDARY_DISTANCE.distance - POD_DISTANCE.distance) / distance
        ) + POD_DISTANCE.distance;
    }

    public int getDistance() {
        return distance;
    }
}
