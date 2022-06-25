package nextstep.subway.fare.domain;

public enum DistanceType {
    EXTRA_10_TO_50(10, 5),
    EXTRA_50(50, 8);

    private final int minDistance;
    private final int extraUnit;

    DistanceType(int minDistance, int extraUnit) {
        this.minDistance = minDistance;
        this.extraUnit = extraUnit;
    }

    public static int distanceExtraFare(int distance) {
        if (distance > EXTRA_50.minDistance) {
            return getExtraFare(distance, EXTRA_50.extraUnit);
        }
        if (distance > EXTRA_10_TO_50.minDistance) {
            return getExtraFare(distance, EXTRA_10_TO_50.extraUnit);
        }
        return 0;
    }

    private static int getExtraFare(int distance, int extraUnit) {
        return (int) ((Math.ceil((distance - 1) / extraUnit) + 1) * FareType.EXTRA.getFare());
    }
}
