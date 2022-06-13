package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareDistance {
    OVER_TEN_TO_FIFTY(10, 50, 5, 100),
    OVER_FIFTY(50, Integer.MAX_VALUE, 8, 100);

    private static final int BASIC_DISTANCE = 10;

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

    private static FareDistance of(Distance distance) {
        return Arrays.stream(values())
                .filter(value -> distance.betweenMinMax(value.minDistance, value.maxDistance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static int calculate(int fare, Distance distance) {
        if (distance.getDistance() < BASIC_DISTANCE) {
            return fare;
        }

        FareDistance fareDistance = FareDistance.of(distance);
        return fare + getOverFare(distance.getDistance(), fareDistance);
    }

    private static int getOverFare(int distance, FareDistance fareDistance) {
        return (int) (Math.ceil((distance - fareDistance.minDistance) / fareDistance.overFareDistance) * fareDistance.overFarePrice);
    }
}
