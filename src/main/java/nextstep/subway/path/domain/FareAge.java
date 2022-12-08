package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public enum FareAge {
    NONE(Objects::isNull, fare -> fare),
    BABY(age -> age >= 0 && age < 6, fare -> fare),
    CHILD(age -> age >= 6 && age < 13, fare -> (int) ((fare - 350)*0.5)),
    YOUTH(age -> age >= 13 && age < 19,fare -> (int) ((fare - 350)*0.7)),
    BASIC(age -> age >= 19, fare -> fare);

    private final Predicate<Integer> ageRange;
    private final AgeDiscountPolicy ageDiscountPolicy;

    FareAge(Predicate<Integer> ageRange, AgeDiscountPolicy ageDiscountPolicy) {
        this.ageRange = ageRange;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public static FareAge from(Integer age) {
        return Arrays.stream(FareAge.values())
                .filter(type -> type.ageRange.test(age))
                .findFirst()
                .orElse(NONE);
    }

    public AgeDiscountPolicy getAgeDiscountPolicy() {
        return this.ageDiscountPolicy;
    }
}
