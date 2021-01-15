package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum AgePolicy {
    CHILD(6, 13, 350, 50),
    TEENAGER(13, 19, 350, 80),
    STANDARD(0, 200, 0, 100);

    private int minAge;
    private int maxAge;
    private int deductionAmount;
    private int discountRate;

    AgePolicy(int minAge, int maxAge, int deductionAmount, int discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionAmount = deductionAmount;
        this.discountRate = discountRate;
    }

    public static AgePolicy of(int age) {
        return Arrays.stream(values())
                .filter(value -> value.minAge >= age && value.maxAge < age)
                .findFirst()
                .orElse(STANDARD);
    }

    public int getDeductionAmount() {
        return deductionAmount;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
