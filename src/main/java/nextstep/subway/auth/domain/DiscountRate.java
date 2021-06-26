package nextstep.subway.auth.domain;

import java.util.Arrays;

public enum DiscountRate {
    ADULT(19, 1),
    TEENAGER(12, 0.8),
    CHILD(5, 0.5),
    NONE(0, 0);

    private int ageBoundary;
    private double dicountRate;

    DiscountRate(int ageBoundary, double dicountRate) {
        this.ageBoundary = ageBoundary;
        this.dicountRate = dicountRate;
    }

    public static DiscountRate findDiscountRateByAge(int age) {
        return Arrays.stream(values())
            .filter(discountRate -> discountRate.ageOverBoundary(age))
            .findFirst()
            .orElse(NONE);
    }

    private boolean ageOverBoundary(int age) {
        return age > ageBoundary;
    }

    public double getDicountRate() {
        return dicountRate;
    }
}
