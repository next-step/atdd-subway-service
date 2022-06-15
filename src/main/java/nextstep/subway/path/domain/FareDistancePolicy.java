package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum FareDistancePolicy {
    MORE_THAN_10_AND_LESS_THAN_50(10, 50, 5, 100),
    MORE_THAN_50(50, Integer.MAX_VALUE, 8, 100);

    public static final int BASIC_CHARGE = 1250;

    private final int min;
    private final int max;
    private final int overFare;
    private final int distanceStandardValue;

    FareDistancePolicy(int min, int max, int distanceStandardValue, int overFare) {
        this.min = min;
        this.max = max;
        this.overFare = overFare;
        this.distanceStandardValue = distanceStandardValue;
    }

    public static Optional<FareDistancePolicy> findFarePolicyByDistance(int distance) {
        return Arrays.stream(values()).filter(value -> value.isRange(distance)).findFirst();
    }

    private boolean isRange(int distance) {
        return distance > min && distance <= max;
    }

    public int getOverFare() {
        return overFare;
    }

    public int getDistanceStandardValue() {
        return distanceStandardValue;
    }
}
