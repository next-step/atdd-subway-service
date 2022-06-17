package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum FareAgePolicy {
    TEENAGER(age -> 13 <= age && age < 19, 350, 0.8),
    CHILD(age -> 6 <= age && age < 13, 350, 0.5);

    private final Predicate<Integer> isValidate;
    private final int deduction;
    private final double discountFare;

    FareAgePolicy(Predicate<Integer> isValidate, int deduction, double discountFare) {
        this.isValidate = isValidate;
        this.deduction = deduction;
        this.discountFare = discountFare;
    }

    private static Optional<FareAgePolicy> findFarePolicyByAge(int age) {
        return Arrays.stream(values()).filter(value -> value.isValidate.test(age)).findFirst();
    }

    public static int calculateDiscountFare(int fare, int age) {
        Optional<FareAgePolicy> farePolicy = FareAgePolicy.findFarePolicyByAge(age);

        return farePolicy
                .map(fareAgePolicy -> (int) ((fare - fareAgePolicy.getDeduction()) * fareAgePolicy.getDiscountFare() + fareAgePolicy.getDeduction()))
                .orElse(fare);
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountFare() {
        return discountFare;
    }
}
