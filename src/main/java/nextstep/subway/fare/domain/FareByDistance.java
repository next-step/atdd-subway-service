package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

public enum FareByDistance {
    OTHERS(50, Integer.MAX_VALUE, 8),
    MEDIUM(10, 50, 5),
    SHORT(0, 10, 0);

    int startDistance;
    int endDistance;
    int fareDistanceUnit;
    static SubwayFare DEFAULT_FARE = new SubwayFare(1250);
    static SubwayFare ADD_PER_DISTANCE_FARE = new SubwayFare(100);

    FareByDistance(int startDistance, int endDistance, int fareDistanceUnit) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.fareDistanceUnit = fareDistanceUnit;
    }

    public SubwayFare getFare(Distance distance) {
        return getFareByDistance(distance.getDistance());
    }

    private SubwayFare getFareByDistance(int distance) {
        return Arrays.stream(FareByDistance.values())
                .filter(fare -> distance > fare.startDistance)
                .map(fare -> calculateFare(distance, FareByDistance.valueOf(fare.name())))
                .reduce(new SubwayFare(0), (a, b) -> a.plus(b));
    }

    private SubwayFare calculateFare(int distance, FareByDistance fare) {
        if (FareByDistance.SHORT.equals(fare)) {
            return DEFAULT_FARE;
        }

        if (distance > FareByDistance.valueOf(fare.name()).endDistance) {
            distance = FareByDistance.valueOf(fare.name()).endDistance;
        }
        return new SubwayFare((int) (Math.ceil((distance - FareByDistance.valueOf(fare.name()).startDistance - 1)
                / FareByDistance.valueOf(fare.name()).fareDistanceUnit) + 1))
                .multiple(ADD_PER_DISTANCE_FARE);
    }

    public static FareByDistance of(Distance distance) {
        return Arrays.stream(FareByDistance.values())
                .filter(fare -> distance.getDistance() > fare.startDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리가 올바르지 않습니다."));
    }
}
