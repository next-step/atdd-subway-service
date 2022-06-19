package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Discount {
    ADULT(0, 0, age -> age == null || age >= 19),
    CHILD(50, 350, age -> age >= 6 && age < 13),
    TEENAGER(20, 350, age -> age >= 13 && age < 19),
    FREE(100, 0, age -> age < 6);

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
