package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum FareDistancePolicy {
    MORE_THAN_10_AND_LESS_THAN_50(distance -> distance > 10 && distance <= 50, 5, 100),
    MORE_THAN_50(distance -> distance > 50 && distance <= Integer.MAX_VALUE, 8, 100);

    public static final int BASIC_CHARGE = 1250;

    private final Predicate<Integer> isValidate;
    private final int overFare;
    private final int distanceStandardValue;

    FareDistancePolicy(Predicate<Integer> isValidate, int distanceStandardValue, int overFare) {
        this.isValidate = isValidate;
        this.overFare = overFare;
        this.distanceStandardValue = distanceStandardValue;
    }

    public static Optional<FareDistancePolicy> findFarePolicyByDistance(int distance) {
        return Arrays.stream(values()).filter(value -> value.isValidate.test(distance)).findFirst();
    }

    public int getOverFare() {
        return overFare;
    }

    public int getDistanceStandardValue() {
        return distanceStandardValue;
    }
}
