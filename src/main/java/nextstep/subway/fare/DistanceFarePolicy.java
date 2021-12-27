package nextstep.subway.fare;

public enum DistanceFarePolicy {
    DEFAULT_SECTION_DISTANCE(0, 10, 1, 0),
    FIRST_SECTION_DISTANCE(10, 50,5, 100),
    SECOND_SECTION_DISTANCE(50, Integer.MAX_VALUE,8, 100);

    private static final int BASE_FARE = 1_250;
    private int from;
    private int limit;
    private int perDistance;
    private int unitFare;

    DistanceFarePolicy(int from, int limit, int perDistance, int unitFare) {
        this.from = from;
        this.limit = limit;
        this.perDistance = perDistance;
        this.unitFare = unitFare;
    }

    public static int calculateOverFare(int distance) {
        int overFare = BASE_FARE;
        for (DistanceFarePolicy farePolicy : DistanceFarePolicy.values()) {
            overFare += calculateFarePerDistance(distance, farePolicy);
        }
        return overFare;
    }

    private static int calculateFarePerDistance(int distance, DistanceFarePolicy farePolicy) {
        if (distance < farePolicy.from) {
            return 0;
        }
        int overSecondSectionDistance = Math.min(distance, farePolicy.limit) - farePolicy.from;
        return (int) ((Math.ceil((overSecondSectionDistance - 1) / farePolicy.perDistance) + 1) * farePolicy.unitFare);
    }
}
