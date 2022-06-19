package nextstep.subway.path.domain;

public enum FareDistanceType {
    BASIC_TYPE(0, 0, 1250),
    DISTANCE_TYPE_1(10, 5, 1250),
    DISTANCE_TYPE_2(50, 8, 2050);

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
        if (DISTANCE_TYPE_2.minDistance < distance) {
            return DISTANCE_TYPE_2;
        }
        if (DISTANCE_TYPE_1.minDistance < distance) {
            return DISTANCE_TYPE_1;
        }
        return BASIC_TYPE;
    }

    public int calculateFare(int distance) {
        if(this == BASIC_TYPE){
            return basicFare;
        }
        return basicFare +
                (int) ((Math.ceil((distance - minDistance - 1) / unitDistance) + 1)
                        * ADDITIONAL_FARE_PER_UNIT);
    }
}
