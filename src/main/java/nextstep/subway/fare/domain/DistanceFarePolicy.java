package nextstep.subway.fare.domain;

import static java.lang.Integer.MAX_VALUE;

import java.util.Arrays;

public enum DistanceFarePolicy {
    LONG_DISTANCE(50, MAX_VALUE, 8),
    MIDDLE_DISTANCE(10, 50, 5),
    SHORT_DISTANCE(0, 10, 0);

    private int startDistance;
    private int endDistance;
    private int additionalFareRange;

    DistanceFarePolicy(int startDistance, int endDistance, int additionalFareRange) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.additionalFareRange = additionalFareRange;
    }

    public static DistanceFarePolicy from(int distance) {
        return Arrays.stream(values())
            .filter(it -> it.startDistance < distance && it.endDistance >= distance)
            .findFirst()
            .orElse(SHORT_DISTANCE);
    }

    public Fare apply(final int distance) {
        if (SHORT_DISTANCE == this) {
            return Fare.FREE;
        }
        int additionalDistance = distance - this.startDistance;
        return new Fare((int) ((Math.ceil((additionalDistance - 1) / additionalFareRange) + 1) * 100));
    }
}
