package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum DistanceExtraFare {
    BASIC(1, 10, 1, 0),
    MEDIUM(11, 50, 5, 100),
    LONG(51, Integer.MAX_VALUE, 8, 100);

    public static final int BASE_FARE = 1_250;
    private final int from;
    private final int to;
    private final int unitDistance;
    private final int unitExtra;

    DistanceExtraFare(int from, int to, int unitDistance, int unitExtra) {
        this.from = from;
        this.to = to;
        this.unitDistance = unitDistance;
        this.unitExtra = unitExtra;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getUnitDistance() {
        return unitDistance;
    }

    public int getUnitExtra() {
        return unitExtra;
    }

    public static DistanceExtraFare valueOf(int distance) {
        return Arrays.stream(DistanceExtraFare.values())
                .filter(distanceExtraFare -> distanceExtraFare.has(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 길이입니다."));
    }

    private boolean has(int distance) {
        return distance > from && distance <= to;
    }
}
