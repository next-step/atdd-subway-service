package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFare {
    TO_FIFTY(11, 50, 5, 100),
    OVER_FIFTY(51, Integer.MAX_VALUE, 8, 100);

    private static final int BASIC_DISTANCE = 10;

    private int minDistance;
    private int maxDistance;
    private int overFareDistance;
    private int overFarePrice;

    DistanceFare(int minDistance, int maxDistance, int overFareDistance, int overFarePrice) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.overFareDistance = overFareDistance;
        this.overFarePrice = overFarePrice;
    }

    public static DistanceFare of(int distance) {
        return Arrays.stream(values())
            .filter(value -> (value.minDistance <= distance) && (distance <= value.maxDistance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public int calculateOverFare(int distance) {
        return ((distance - BASIC_DISTANCE - 1) / overFareDistance + 1) * overFarePrice;
    }


}
