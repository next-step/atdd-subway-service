package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareAgeDiscount {
    INFANT(6, 13, 0.5),
    TEENAGER(13, 19, 0.2);

    private static final int DEDUCTION_FEE = 350;
    private static final int ADULT = 19;

    private int minAge;
    private int maxAge;
    private double discountRate;

    private FareAgeDiscount(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    private static FareAgeDiscount of(int age) {
        return Arrays.stream(values())
                .filter(value -> (value.minAge <= age) && (age < value.maxAge))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static int calculate(int fare, Age age) {
        if (age.isAdult()) {
            return fare;
        }

        FareAgeDiscount fareAgeDiscount = FareAgeDiscount.of(age.getValue());
        return (int) (fare - fareAgeDiscount.getDiscount(fare, fareAgeDiscount.discountRate));
    }

    private double getDiscount(int fare, double discountRate) {
        return (fare - DEDUCTION_FEE) * discountRate;
    }
}
