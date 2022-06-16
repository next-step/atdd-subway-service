package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareAgeDiscount {
    INFANT(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    ADULT(19, Integer.MAX_VALUE, 0);

    private int minAge;
    private int maxAge;
    private double discountRate;

    private FareAgeDiscount(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static FareAgeDiscount of(int age) {
        return Arrays.stream(values())
                .filter(value -> (value.minAge <= age) && (age < value.maxAge))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
