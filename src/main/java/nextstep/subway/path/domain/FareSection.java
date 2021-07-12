package nextstep.subway.path.domain;

public enum FareSection {
    BASIC(10, 1, 0),
    ADVANCED(40, 5, 100),
    FINAL(Integer.MAX_VALUE, 8, 100);

    public static long BASIC_FARE = 1250L;
    public static int ZERO_DISTANCE = 0;
    public static long ZERO_FARE = 0L;

    private int maxDistance;
    private int unitDistance;
    private int unitFare;

    FareSection(int maxDistance, int unitDistance, int unitFare) {
        this.maxDistance = maxDistance;
        this.unitDistance = unitDistance;
        this.unitFare = unitFare;
    }

    public int maxDistance() {
        return maxDistance;
    }

    private long calculate(int distance) {
        if (distance > ZERO_DISTANCE) {
            int calculateDistance = Math.min(distance, maxDistance);
            return calculateDistance / unitDistance * unitFare;
        }
        return ZERO_FARE;
    }
    public static Fare calculateFare(int distance) {
        long calculateFare = BASIC_FARE;

        for (FareSection section : FareSection.values()) {
            calculateFare += section.calculate(distance);
            distance -= section.maxDistance();
        }
        return new Fare(calculateFare);
    }
}
