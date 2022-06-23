package nextstep.subway.member.domain;

import java.util.Arrays;
import java.util.Optional;

public enum AgeSale {
    CHILDREN(0.5, 6, 13),
    YOUTH(0.8, 13, 19);

    private static final int AGE_DEDUCTION = 350;
    private final double salePercent;
    private final int greaterThenOrEqualAge;
    private final int lessThenAge;

    AgeSale(double salePercent, int greaterThenOrEqualAge, int lessThenAge) {
        this.salePercent = salePercent;
        this.greaterThenOrEqualAge = greaterThenOrEqualAge;
        this.lessThenAge = lessThenAge;
    }

    public static Optional<AgeSale> of(int age) {
        return Arrays.stream(AgeSale.values())
                .filter(it -> age >= it.greaterThenOrEqualAge && age < it.lessThenAge)
                .findFirst();
    }

    public int calculate(int fare) {
        return (int) Math.round((fare - AGE_DEDUCTION) * salePercent);
    }
}
