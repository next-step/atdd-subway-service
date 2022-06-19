package nextstep.subway.path.domain;

public enum FareDistanceType {
    BASIC_DISTANCE(0, 0, 1250),
    MIDDLE_DISTANCE(10, 5, 1250),
    LONG_DISTANCE(50, 8, 2050);

    private static int ADDITIONAL_FARE_PER_UNIT = 100;

    private final int minDistance;
    private final int unitDistance;
    private final int basicFare;

    FareDistanceType(int minDistance, int unitDistance, int basicFare) {
        this.minDistance = minDistance;
        this.unitDistance = unitDistance;
        this.basicFare = basicFare;
    }

    public static FareDistanceType typeOf(int distance) {
        if (LONG_DISTANCE.minDistance < distance) {
            return LONG_DISTANCE;
        }
        if (MIDDLE_DISTANCE.minDistance < distance) {
            return MIDDLE_DISTANCE;
        }
        return BASIC_DISTANCE;
    }

    public int calculateFare(int distance) {
        if(this == BASIC_DISTANCE){
            return basicFare;
        }
        return basicFare +
                (int) ((Math.ceil((distance - minDistance - 1) / unitDistance) + 1)
                        * ADDITIONAL_FARE_PER_UNIT);
    }
}
