package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum AgeDiscount {
    STUDENT(13, 19, 350, 0.2),
    CHILD(6, 13, 350, 0.5);

    private final int overAge;
    private final int underAge;
    private final int deduction;
    private final double discountRate;

    AgeDiscount(int overAge, int underAge, int deduction, double discountRate) {
        this.overAge = overAge;
        this.underAge = underAge;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static AgeDiscount of(int age) {
        return Arrays.stream(values())
                .filter(ageDiscount -> ageDiscount.has(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 길이입니다."));
    }

    private boolean has(int age) {
        return age >= overAge && age < underAge;
    }

    public int getOverAge() {
        return overAge;
    }

    public int getUnderAge() {
        return underAge;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
