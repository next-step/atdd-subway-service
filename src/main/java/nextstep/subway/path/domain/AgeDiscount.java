package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;

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

    public static Fare calculate(int age, int fare) {
        AgeDiscount ageDiscount = findDiscountByAge(age);
        return new Fare((int) ((fare - DEFAULT_DISCOUNT_FARE) * ageDiscount.rate));
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
