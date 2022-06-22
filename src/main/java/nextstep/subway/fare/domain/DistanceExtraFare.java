package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

public enum DistanceExtraFare {
    BASIC(1, 10, 1, 0),
    MEDIUM(11, 50, 5, 100),
    LONG(51, Integer.MAX_VALUE, 8, 100);

    public static final int BASE_FARE = 1_250;
    private static final DistanceExtraFare DEFAULT = DistanceExtraFare.BASIC;
    private final int from;
    private final int border;
    private final int unitDistance;
    private final int unitExtra;

    DistanceExtraFare(int from, int border, int unitDistance, int unitExtra) {
        this.from = from;
        this.border = border;
        this.unitDistance = unitDistance;
        this.unitExtra = unitExtra;
    }

    public static DistanceExtraFare of(int totalDistance) {
        Distance distance = new Distance(totalDistance);
        return Arrays.stream(values())
                .filter(distanceExtraFare -> distance.getValue() < distanceExtraFare.border)
                .findFirst()
                .orElse(DEFAULT);
    }

    public int addExtraOf(int distance) {
        int result = 0;
        for (DistanceExtraFare distanceExtraFare : DistanceExtraFare.values()) {
            result += distanceExtraFare.getExtraFare(distance);
        }

        return result;
    }

    private int getExtraFare(int distance) {
        if (distance < this.from) {
            return 0;
        }
        int extraDistance = Math.min(distance, this.border) - this.from + 1;
        return this.calculateExtraFare(extraDistance);
    }

    private int calculateExtraFare(int extraDistance) {
        return (int) ((Math.ceil((extraDistance - 1) / unitDistance) + 1) * unitExtra);
    }
}
