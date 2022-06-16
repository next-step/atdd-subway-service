package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareDistance {
    UNDER_TEN(0, 10, 1, 0),
    OVER_TEN_TO_FIFTY(10, 50, 5, 100),
    OVER_FIFTY(50, Integer.MAX_VALUE, 8, 100);

    private int minDistance;
    private int maxDistance;
    private int overFareDistance;
    private int overFarePrice;

    private FareDistance(int minDistance, int maxDistance, int overFareDistance, int overFarePrice) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.overFareDistance = overFareDistance;
        this.overFarePrice = overFarePrice;
    }

    public static FareDistance of(Distance distance) {
        return Arrays.stream(values())
                .filter(value -> distance.betweenMinMax(value.minDistance, value.maxDistance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getOverFareDistance() {
        return overFareDistance;
    }

    public int getOverFarePrice() {
        return overFarePrice;
    }
}
