package nextstep.subway.fares.policy;

import lombok.Getter;

import java.util.Arrays;
import java.util.function.IntPredicate;

@Getter
public enum DiscountByAge {

    TEENAGER(0.2, age -> age >= 13 && age < 19),
    CHILDREN(0.5, age -> age >= 6 && age < 13);

    private final double rate;
    private final IntPredicate predicateAge;

    DiscountByAge(double rate, IntPredicate predicateAge) {
        this.rate = rate;
        this.predicateAge = predicateAge;
    }

    public static double getDiscountRateByAge(int age) {
        return Arrays.stream(values())
                .filter(discountByAge -> discountByAge.predicateAge.test(age))
                .findFirst()
                .map(DiscountByAge::getRate)
                .orElse(0.0);
    }
}
