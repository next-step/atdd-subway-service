package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgeDiscountFare {
    STANDARD(0, age -> false),
    CHILDREN(0.5, age -> age >= 6 && age < 13),
    TEENAGER(0.2, age -> age >= 13 && age < 19);

    public static final int DEDUCT_FARE = 350;
    private double rate;
    private IntPredicate ageRange;

    AgeDiscountFare(double rate, IntPredicate ageRange) {
        this.rate = rate;
        this.ageRange = ageRange;
    }

    public Fare calculateDiscount(Fare additionalFare) {
        Fare deductFare = additionalFare.minus(Fare.of(DEDUCT_FARE));
        Fare discountFare = deductFare.multiply(this.rate);
        return deductFare.minus(discountFare);
    }

    public static AgeDiscountFare findAgeDiscount(int age) {
        return Arrays.stream(values())
                .filter(ageDiscount -> ageDiscount.isIncludedInRange(age))
                .findFirst()
                .orElse(STANDARD);
    }

    private boolean isIncludedInRange(int age) {
        return this.ageRange.test(age);
    }
}
