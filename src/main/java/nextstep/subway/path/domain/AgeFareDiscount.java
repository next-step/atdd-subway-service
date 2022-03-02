package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeFareDiscount {

    CHILD(6, 13, 0.5),
    TEEN(13, 19, 0.2);


    private static final int DEDUCTION_FEE = 350;
    private int minAge;
    private int maxAge;
    private double discount;

    AgeFareDiscount(int minAge, int maxAge, double discount) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discount = discount;
    }

    private static AgeFareDiscount of(int age) {
        return Arrays.stream(values())
            .filter(value -> (value.minAge <= age) && (age < value.maxAge))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static int getAgeDiscountedFare(Integer age, int fare) {
        if (age == null) {
            return fare;
        }

        AgeFareDiscount ageFareDiscount = AgeFareDiscount.of(age);
        fare -= ageFareDiscount.calculateAgeDiscountFare(fare);
        return fare;
    }

    private int calculateAgeDiscountFare(int fare) {
        return (int) ((fare - DEDUCTION_FEE) * discount);
    }
}
