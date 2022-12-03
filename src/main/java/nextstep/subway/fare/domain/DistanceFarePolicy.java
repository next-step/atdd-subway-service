package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    HIGH_DISTANCE_KM(51, DistanceFarePolicy::calculateHighDistanceOverFare),
    MIDDLE_DISTANCE_KM(11, DistanceFarePolicy::calculateMiddleDistanceOverFare),
    DEFAULT_KM(0, (distance) -> 0);

    private static final int AMOUNT_PER_KM = 100;
    private static final int HIGH_ADD_PER_KM = 8;
    private static final int HIGH_EXCEPT_KM = 50;
    private static final int MIDDLE_ADD_PER_KM = 5;
    private static final int MIDDLE_EXCEPT_KM = 10;

    private final int km;
    private final Function<Integer, Integer> discount;

    DistanceFarePolicy(final int km, final Function<Integer, Integer> discount) {
        this.km = km;
        this.discount = discount;
    }

    public static int ofExtraCharge(final int distance) {
        return Arrays.stream(values())
                .filter(it -> it.overDistance(distance))
                .findFirst()
                .orElse(DEFAULT_KM)
                .discount(distance);
    }

    private boolean overDistance(final int distance) {
        return km < distance;
    }

    public int discount(final int distance) {
        return discount.apply(distance);
    }

    private static int calculateHighDistanceOverFare(final int distance) {
        final int calculateDistance =  distance - HIGH_EXCEPT_KM;
        return (int) ((Math.ceil((calculateDistance - 1) / HIGH_ADD_PER_KM) + 1) * AMOUNT_PER_KM) + calculateMiddleDistanceOverFare(HIGH_EXCEPT_KM);
    }

    private static int calculateMiddleDistanceOverFare(final int distance) {
        final int calculateDistance = distance - MIDDLE_EXCEPT_KM;
        return (int) ((Math.ceil((calculateDistance - 1) / MIDDLE_ADD_PER_KM) + 1) * AMOUNT_PER_KM);
    }

}
