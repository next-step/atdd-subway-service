package nextstep.subway.line.domain;

import java.util.Arrays;

public enum DistanceSurchargePolicy {
    FIFTY(50, 100, 8),
    TEN(10, 100, 5),
    BASIC(0, 0, 0);

    private final int minDistance;
    private final int surcharge;
    private final int referenceDistance;

    DistanceSurchargePolicy(int minDistance, int surcharge, int referenceDistance) {
        this.minDistance = minDistance;
        this.surcharge = surcharge;
        this.referenceDistance = referenceDistance;
    }

    public static DistanceSurchargePolicy of(Distance distance) {
        return Arrays.stream(DistanceSurchargePolicy.values())
                .filter(it -> it.isCorrectDistance(distance))
                .findFirst()
                .orElse(BASIC);

    }

    public Fee calculate(Fee fee, Distance distance) {
        return Fee.of(fee.get() + calculateOverFare(distance.get()-10));
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1d) / referenceDistance) + 1) * surcharge);
    }

    private boolean isCorrectDistance(Distance distance) {
        return distance.get() > minDistance;
    }
}
