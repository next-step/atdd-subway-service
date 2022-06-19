package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Discount {
    ADULT(0, 0, n -> n == null || n >= 19),
    CHILD(50, 350, n -> n >= 6 && n < 13),
    TEENAGER(20, 350, n -> n >= 13 && n < 19),
    FREE(100, 0, n -> n < 6);

    private final int discountRate;
    private final int discount;
    private final Predicate<Integer> ageMatchingExp;

    Discount(int discountRate, int discount, Predicate<Integer> ageMatchingExp) {
        this.discountRate = discountRate;
        this.discount = discount;
        this.ageMatchingExp = ageMatchingExp;
    }

    public static Discount of(Integer age) {
        return Arrays.stream(values())
                .filter(s -> s.ageMatchingExp.test(age))
                .findFirst()
                .orElse(ADULT);
    }

    public int calculate(int fare) {
        return (int) ((fare - discount) * (1 - (discountRate * 0.01)));
    }
}
