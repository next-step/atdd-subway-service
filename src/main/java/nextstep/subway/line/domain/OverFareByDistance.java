package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;

import static nextstep.subway.line.domain.Distance.MIN_DISTANCE;

public enum OverFareByDistance {
    SECOND(50, 8, 57),
    FIRST(10, 5, 10),
    ;

    private static final int MIN_OVER_FARE = 0;
    private static final int PER_FARE = 100;
    private static final int CALCULATE_OVER_FARE_OFFSET = 1;

    private final int minDistance;
    private final int OverFarePerDistance;
    private final int distanceOffset;

    OverFareByDistance(int minDistance, int overFarePerDistance, int distanceOffset) {
        this.minDistance = minDistance;
        this.OverFarePerDistance = overFarePerDistance;
        this.distanceOffset = distanceOffset;
    }

    public static Optional<OverFareByDistance> of(int distance) {
        return Arrays.stream(OverFareByDistance.values())
                .filter(it -> distance > it.minDistance)
                .findFirst();
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int calculateOverFare(int distance) {
        int appliedOffsetDistance = distance - distanceOffset;
        if (appliedOffsetDistance <= MIN_DISTANCE) {
            return MIN_OVER_FARE;
        }
        return (int) ((Math.ceil((appliedOffsetDistance - CALCULATE_OVER_FARE_OFFSET) / OverFarePerDistance) + CALCULATE_OVER_FARE_OFFSET) * PER_FARE);
    }
}
