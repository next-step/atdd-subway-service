package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DiscountAge {
    CHILD(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    ADULT(19, 100, 0);

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private final int start;
    private final int end;
    private final double percent;

    DiscountAge(int start, int end, double percent) {
        this.start = start;
        this.end = end;
        this.percent = percent;
    }

    public static int calculate(int age, int fare) {
        DiscountAge discountAge = find(age);
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * discountAge.percent);
    }

    private static DiscountAge find(int age) {
        return Arrays.stream(DiscountAge.values())
                .filter(discountAge -> discountAge.isBetween(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean isBetween(int age) {
        return start <= age && age < end;
    }
}
