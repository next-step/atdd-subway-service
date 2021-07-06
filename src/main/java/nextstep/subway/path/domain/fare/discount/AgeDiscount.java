package nextstep.subway.path.domain.fare.discount;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeDiscount implements Discount {
    TODDLER(age -> age >= 0 && age < 6, fare -> 0),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(age -> age >= 19, fare -> fare);

    private final Predicate<Integer> selector;
    private final IntUnaryOperator discountCalculator;

    AgeDiscount(Predicate<Integer> selector, IntUnaryOperator discountCalculator) {
        this.selector = selector;
        this.discountCalculator = discountCalculator;
    }

    public static Discount from(Integer age) {
        return Arrays.stream(values())
                .filter(ageDiscount -> ageDiscount.selector.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public int discount(int fare) {
        return this.discountCalculator.applyAsInt(fare);
    }
}
