package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum AgeDiscountFarePolicy {
    NORMAL_FREE(65, Integer.MAX_VALUE, 0, 100),
    NORMAL(19, 64, 0, 0),
    TEENAGER(13, 18, 350, 20),
    CHILDREN(6, 12, 350, 50),
    CHILDREN_FREE(1, 5, 0, 100);
    ;

    private final int startAge;
    private final int endAge;
    private final Fare discountFare;
    private final int discountPercent;

    AgeDiscountFarePolicy(int startAge, int endAge, int discountFare, int discountPercent) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.discountFare = Fare.of(discountFare);
        this.discountPercent = discountPercent;
    }

    public static AgeDiscountFarePolicy of(int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.isAssignable(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Fare apply(Fare fare) {
        return fare.minus(discountFare).discountByPercent(discountPercent);
    }

    public int endAge() {
        return this.endAge;
    }

    private boolean isAssignable(int age) {
        return this.startAge <= age
                && age <= this.endAge;
    }
}
