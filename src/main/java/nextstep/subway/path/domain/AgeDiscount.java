package nextstep.subway.path.domain;

import java.util.Arrays;
import nextstep.subway.line.domain.ExtraFare;

public enum AgeDiscount {
    CHILD(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    ADULT(19, 100, 0);

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private final int start;
    private final int end;
    private final double rate;

    AgeDiscount(int start, int end, double rate) {
        this.start = start;
        this.end = end;
        this.rate = rate;
    }

    public static ExtraFare calculate(int age, int fare) {
        AgeDiscount ageDiscount = findDiscountByAge(age);
        return ExtraFare.from((int) ((fare - DEFAULT_DISCOUNT_FARE) * ageDiscount.rate));
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
