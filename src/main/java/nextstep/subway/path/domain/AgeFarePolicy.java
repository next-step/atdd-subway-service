package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum AgeFarePolicy {
    TEENAGER(13, 19, 0.2, 350),
    CHILDREN(6, 13, 0.5, 350);

    private final int ageFrom;
    private final int ageToExclusive;
    private final double discountRate;
    private final int deduction;

    AgeFarePolicy(int ageFrom, int ageToExclusive, double discountRate, int deduction) {
        this.ageFrom = ageFrom;
        this.ageToExclusive = ageToExclusive;
        this.discountRate = discountRate;
        this.deduction = deduction;
    }

    public static Optional<AgeFarePolicy> findApplicableAgeFarePolicy(final int age) {
        return Arrays.stream(values())
                .filter(ageFarePolicy -> ageFarePolicy.isApplicable(age))
                .findAny();
    }

    private boolean isApplicable(int age) {
        return ageFrom <= age && age < ageToExclusive;
    }

    public int applyPolicy(int totalFare) {
        return (int) ((totalFare - deduction) * (1 - discountRate));
    }
}
