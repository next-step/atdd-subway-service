package nextstep.subway.fare;

import java.util.Arrays;
import org.apache.commons.lang3.Range;

public enum FareDiscount {
    TEENAGER(Range.between(13, 18), 0.2),
    CHILDREN(Range.between(6, 12), 0.5),
    NONE(null, 0);

    private final Range<Integer> ageRange;
    private final double discountRate;

    FareDiscount(Range<Integer> ageRange, double discountRate) {
        this.ageRange = ageRange;
        this.discountRate = discountRate;
    }

    private static FareDiscount getByAge(int age) {
        return Arrays.stream(values())
            .filter(fareDiscount -> fareDiscount.ageRange != null)
            .filter(fareDiscount -> fareDiscount.ageRange.contains(age))
            .findFirst()
            .orElse(NONE);
    }

    public static double getDiscountRateByAge(int age) {
        return getByAge(age).discountRate;
    }
}
