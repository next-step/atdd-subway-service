package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeFarePolicy {
    CHILDREN(6, 13, 0.5),
    YOUTH(13, 19, 0.2),
    ADULT(19, Integer.MAX_VALUE, 0);

    private static final Fare DEDUCT_FARE = Fare.of(350);
    private final int minAge;
    private final int maxAge;
    private final double discountRate;

    AgeFarePolicy(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(it -> age >= it.minAge && age < it.maxAge)
                .findFirst()
                .orElse(ADULT);
    }

    public Fare discount(Fare fare) {
        if (discountRate > 0) {
            fare = fare.minus(DEDUCT_FARE).discountByPercent(discountRate);
        }
        return fare;
    }
}
