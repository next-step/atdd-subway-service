package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

public enum DistanceExtraFare {
    BASIC(10, 1, 0),
    MEDIUM(50, 5, 100),
    LONG(Integer.MAX_VALUE, 8, 100);

    public static final int BASE_FARE = 1_250;
    private static final DistanceExtraFare DEFAULT = DistanceExtraFare.BASIC;
    private final int border;
    private final int unitDistance;
    private final int unitExtra;

    DistanceExtraFare(int border, int unitDistance, int unitExtra) {
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
        for (DistanceExtraFare distanceExtraFare : values()) {
            result += distanceExtraFare.getExtraFare(distance);
        }

        return result;
    }

    private int getExtraFare(int distance) {
        int prevOrdinal = ordinal() - 1;
        if (prevOrdinal < 0) {
            return 0;
        }

        DistanceExtraFare prev = values()[prevOrdinal];
        if (distance <= prev.border) {
            return 0;
        }

        int extraDistance = Math.min(distance, this.border) - prev.border;
        return this.calculateExtraFare(extraDistance);
    }

    private int calculateExtraFare(int extraDistance) {
        return (int) ((Math.ceil((extraDistance - 1) / unitDistance) + 1) * unitExtra);
    }
}
