package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscount {
    CHILD(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    ADULT(19, 100, 0);

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private final int start;
    private final int end;
    private final double percent;

    AgeDiscount(int start, int end, double percent) {
        this.start = start;
        this.end = end;
        this.percent = percent;
    }

    public static int calculate(int age, int fare) {
        AgeDiscount ageDiscount = findDiscountByAge(age);
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * ageDiscount.percent);
    }

    private static AgeDiscount findDiscountByAge(int age) {
        return Arrays.stream(AgeDiscount.values())
                .filter(ageDiscount -> ageDiscount.isIncluded(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean isIncluded(int age) {
        return start <= age && age < end;
    }
}
