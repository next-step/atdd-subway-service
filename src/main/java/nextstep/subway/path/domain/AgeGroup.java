package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeGroup {

    NONE(0, age -> age >= 19),
    TEENAGER(0.2, age -> age >= 13 && age < 19),
    CHILDREN(0.5, age -> age >= 6 && age < 13);

    private double discountPercent;
    private Function<Integer, Boolean> isContainsAgeFunction;

    AgeGroup(double discountPercent, Function<Integer, Boolean> isContainsAge) {
        this.discountPercent = discountPercent;
        this.isContainsAgeFunction = isContainsAge;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(values())
                .filter(value -> value.isContainsAgeFunction.apply(age))
                .findAny().orElse(NONE);
    }
}
