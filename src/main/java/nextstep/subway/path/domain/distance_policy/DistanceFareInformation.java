package nextstep.subway.path.domain.distance_policy;

import java.util.Arrays;

public enum DistanceFareInformation {

    BASIC(0, 10, new BasicDistanceFarePolicy()),
    OVER10KM(10, 50, new Over10KmDistanceFarePolicy()),
    OVER50KM(50, 300, new Over50KmDistanceFarePolicy());

    private int minDistance;
    private int maxDistance;
    private DistanceFarePolicy distanceFarePolicy;

    DistanceFareInformation(int minDistance, int maxDistance, DistanceFarePolicy distanceFarePolicy) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.distanceFarePolicy = distanceFarePolicy;
    }

    public static DistanceFarePolicy distanceFarePolicy(int distance) {
        DistanceFarePolicy distanceFarePolicy = Arrays.stream(values())
                .filter(distanceFare ->
                        distanceFare.minDistance() <= distance &&
                                distance <= distanceFare.maxDistance()
                ).findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .distanceFarePolicy();

        distanceFarePolicy.setDistance(distance);

        return distanceFarePolicy;
    }

    public int minDistance() {
        return minDistance;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public DistanceFarePolicy distanceFarePolicy() {
        return distanceFarePolicy;
    }
}
