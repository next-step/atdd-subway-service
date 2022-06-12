package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareAgeDiscount {
    INFANT(6, 13, 0.5),
    TEENAGER(13, 19, 0.2);

    private static final int DEDUCTION_FEE = 350;
    private static final int ADULT = 19;

    private int minAge;
    private int maxAge;
    private double discount;

    FareAgeDiscount(int minAge, int maxAge, double discount) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discount = discount;
    }

    private static FareAgeDiscount of(int age) {
        return Arrays.stream(values())
                .filter(value -> (value.minAge <= age) && (age < value.maxAge))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static int culculate(int fare, int age) {
        if (age == 0 || age >= ADULT) {
            return fare;
        }

        FareAgeDiscount fareAgeDiscount = FareAgeDiscount.of(age);
        return (int) (fare - ((fare - DEDUCTION_FEE) * fareAgeDiscount.discount));
    }
}
