package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum FareAgePolicy {
    TEENAGER(13, 19, 350, 0.8),
    CHILD(6, 13, 350, 0.5);

    private final int min;
    private final int max;
    private final int deduction;
    private final double discountFare;

    FareAgePolicy(int min, int max, int deduction, double discountFare) {
        this.min = min;
        this.max = max;
        this.deduction = deduction;
        this.discountFare = discountFare;
    }

    public static Optional<FareAgePolicy> findFarePolicyByAge(int age) {
        return Arrays.stream(values()).filter(value -> value.isRange(age)).findFirst();
    }

    private boolean isRange(int age) {
        return age >= min && age < max;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountFare() {
        return discountFare;
    }
}
