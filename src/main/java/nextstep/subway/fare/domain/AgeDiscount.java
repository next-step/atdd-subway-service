package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum AgeDiscount {
    CHILD(13, 350, 0.5),
    STUDENT(19, 350, 0.2),
    ADULT(0, 0, 0);

    public static final AgeDiscount DEFAULT = AgeDiscount.ADULT;
    private static final int MINIMUM_AGE = 0;
    private final int limit;
    private final int deduction;
    private final double discountRate;

    AgeDiscount(int limit, int deduction, double discountRate) {
        this.limit = limit;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static AgeDiscount of(int age) {
        validate(age);
        return Arrays.stream(values())
                .filter(ageDiscount -> age < ageDiscount.limit)
                .findFirst()
                .orElse(DEFAULT);
    }

    private static void validate(int age) {
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 0보다 커야 합니다.");
        }
    }

    public double calculateDiscount(int baseFare) {
        return (baseFare - deduction) * discountRate;
    }
}
