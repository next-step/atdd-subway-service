package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DistanceFare {
    TO_FIFTY(10, 50, 5, 100),
    OVER_FIFTY(50, Integer.MAX_VALUE, 8, 100);

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
            .filter(value -> (value.minDistance < distance) && (distance <= value.maxDistance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static int getOverFare(int distance) {
        return Arrays.stream(values())
            .mapToInt(value -> value.calculateOverFare(
                Math.min(distance - value.minDistance, value.maxDistance - value.minDistance)))
            .sum();
    }

    public int calculateOverFare(int overDistance) {
        if (overDistance <= 0) {
            return 0;
        }
        return ((overDistance - 1) / overFareDistance + 1) * overFarePrice;
    }


}
