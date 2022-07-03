package nextstep.subway.path.domain;

public enum DistanceFarePolicy {
    BASIC(0, 0),
    OVER_10KM_AND_UNDER_50KM(10,  5),
    OVER_50KM(50, 8);

    private final int over;
    private final int each;
    private static final int ADDITIONAL_FARE = 100;
    public static final int BASIC_FARE = 1250;

    DistanceFarePolicy(int over, int each) {
        this.over = over;
        this.each = each;
    }

    private static DistanceFarePolicy getFarePolicyByDistance(int distance) {
        if (distance > OVER_50KM.over) {
            return OVER_50KM;
        }

        if (distance > OVER_10KM_AND_UNDER_50KM.over) {
            return OVER_10KM_AND_UNDER_50KM;
        }

        return BASIC;
    }

    public static int calculateOverFare(int distance) {
        DistanceFarePolicy farePolicy = getFarePolicyByDistance(distance);
        if (farePolicy.each == 0) {
            return 0;
        }

        return (int) ((Math.ceil((distance - 1) / farePolicy.each) + 1) * DistanceFarePolicy.ADDITIONAL_FARE);
    }

}
