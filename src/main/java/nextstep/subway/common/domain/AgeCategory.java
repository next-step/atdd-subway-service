package nextstep.subway.common.domain;

import java.util.Arrays;
import java.util.function.Predicate;

enum AgeCategory {

    INFANT(age -> age < 6),
    CHILD(age -> 6 <= age && age < 13),
    YOUTH(age -> 13 <= age && age < 19),
    ADULT(age -> 19 <= age);

    private final Predicate<Integer> condition;

    AgeCategory(Predicate<Integer> condition) {
        this.condition = condition;
    }

    static AgeCategory valueOf(int age) {
        return Arrays.stream(values()).
            filter(category -> category.condition.test(age))
            .findFirst()
            .orElse(ADULT);
    }

    boolean isYouth() {
        return this == YOUTH;
    }

    boolean isChild() {
        return this == CHILD;
    }
}
