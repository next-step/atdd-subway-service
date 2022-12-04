package nextstep.subway.path.domain.age;

import java.util.Arrays;
import nextstep.subway.line.domain.ExtraFare;

public enum AgeDiscount implements FareDeductible {
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
        return ExtraFare.from(ageDiscount.calculateDeductedFare(fare));
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

    @Override
    public int calculateDeductedFare(int originalFare) {
        return (int) ((originalFare - DEFAULT_DISCOUNT_FARE) * this.rate);
    }
}
