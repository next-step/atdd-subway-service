package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    FIFTY_KM(51, (distance) -> calculateFiFtyOverFare(distance - 50) + calculateTenOverFare(40)),
    TEN_KM(11, (distance) -> calculateTenOverFare(distance - 10)),
    DEFAULT_KM(0, (distance) -> 0);

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


    private static int calculateFiFtyOverFare(final int distance) {
        return distance / 8 * 100;
    }

    private static int calculateTenOverFare(final int distance) {
        return distance / 5 * 100;
    }

}
