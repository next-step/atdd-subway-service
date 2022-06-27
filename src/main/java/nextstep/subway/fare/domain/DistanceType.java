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
        int extraFare = FareType.BASIC.getFare();
        if (distance > EXTRA_50.minDistance) {
            extraFare += EXTRA_50.extraFare(distance);
            distance = EXTRA_50.minDistance;
        }
        if (distance > EXTRA_10_TO_50.minDistance) {
            extraFare += EXTRA_10_TO_50.extraFare(distance);
        }
        return extraFare;
    }

    public int extraFare(int distance) {
        int overDistance = distance - minDistance;
        return (int) ((Math.ceil((overDistance - 1) / extraUnit) + 1) * FareType.EXTRA.getFare());
    }
}
