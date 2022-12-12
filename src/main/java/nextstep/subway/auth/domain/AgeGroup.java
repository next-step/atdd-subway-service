package nextstep.subway.auth.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeGroup {

    NO_SALE_AGE(0, 1, age -> age >= 19 || age < 6),
    YOUTH(350, 0.8, age -> age >= 13 && age < 19),
    CHILD(350, 0.5, age -> age >= 6 && age < 13);

    private final int deductFare;
    private final double sale;
    private final Function<Integer, Boolean> match;

    AgeGroup(int deductFare, double sale, Function<Integer, Boolean> match) {
        this.deductFare = deductFare;
        this.sale = sale;
        this.match = match;
    }

    public static AgeGroup findByAge(Integer age) {
        return Arrays.stream(AgeGroup.values())
            .filter(ageGroup -> ageGroup.match.apply(age))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public int applySale(int fare) {
        return (int)((fare - deductFare) * sale);
    }
}
