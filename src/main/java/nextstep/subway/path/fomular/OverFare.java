package nextstep.subway.path.fomular;

import nextstep.subway.line.domain.Distance;

public enum OverFare {
    DISTANCE_10_KM(10, 5),
    DISTANCE_50_KM(50, 8);

    private static final int DEFAULT_USE_FARE_AMOUNT = 1250;

    int overDistance;
    int discountPer;

    OverFare(int overDistance, int discountPer) {
        this.overDistance = overDistance;
        this.discountPer = discountPer;
    }

    public static int calculate(Distance distance) {
        int overAmount = DEFAULT_USE_FARE_AMOUNT;
        if (isOver(distance, DISTANCE_10_KM.overDistance)) {
            overAmount += OverFare.DISTANCE_10_KM.calculateOverFare((Math.min(distance.toInt(), DISTANCE_50_KM.overDistance)));
        }
        if (isOver(distance, DISTANCE_50_KM.overDistance)) {
            overAmount += OverFare.DISTANCE_50_KM.calculateOverFare(distance.toInt());
        }
        return overAmount;
    }

    public int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - this.overDistance - 1) / this.discountPer) + 1) * 100);
    }

    private static boolean isOver(Distance distance, int overDistance) {
        return distance.isOver(overDistance);
    }
}
