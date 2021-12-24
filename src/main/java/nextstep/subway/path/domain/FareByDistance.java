package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

public enum FareByDistance {
    OTHERS(50, Integer.MAX_VALUE, 8),
    MEDIUM(10, 50, 5),
    SHORT(0, 10, 0);

    int startDistance;
    int endDistance;
    int fareDistanceUnit;
    static int DEFAULT_FARE = 1250;
    static int ADD_PER_DISTANCE_FARE = 100;

    FareByDistance(int startDistance, int endDistance, int fareDistanceUnit) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.fareDistanceUnit = fareDistanceUnit;
    }

    public static int getFare(Distance distance) {
        return getFareByDistance(distance.getDistance());
    }

    private static int getFareByDistance(int distance) {
        return Arrays.stream(FareByDistance.values())
                .filter(fare -> distance > fare.startDistance)
                .map(fare -> calculateFare(distance, FareByDistance.valueOf(fare.name())))
                .reduce(0, (a, b) -> a + b);
    }

    private static int calculateFare(int distance, FareByDistance fare) {
        if (FareByDistance.SHORT.equals(fare)) {
            return DEFAULT_FARE;
        }

        if (distance > FareByDistance.valueOf(fare.name()).endDistance) {
            distance = FareByDistance.valueOf(fare.name()).endDistance;
        }
        return (int) ((Math.ceil((distance - FareByDistance.valueOf(fare.name()).startDistance - 1)
                / FareByDistance.valueOf(fare.name()).fareDistanceUnit) + 1)
                * ADD_PER_DISTANCE_FARE);
    }

}
