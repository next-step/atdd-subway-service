package nextstep.subway.path.domain;

import java.util.Arrays;

public enum FarePolicy {

    ADULT(19, 200, 1_250, 0, 0),
    TEENAGER(13, 18, 1_250, 350, 20),
    CHILD(6, 12, 1_250, 350, 50),
    BABY(0, 5, 0, 0, 0);

    private final int minAge;
    private final int maxAge;
    private final int defaultAmount;
    private final int discountAmount;
    private final int discountRate;

    FarePolicy(int minAge, int maxAge, int defaultAmount, int discountAmount, int discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.defaultAmount = defaultAmount;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
    }

    public static FarePolicy get(int age) {
        return Arrays.stream(values())
            .filter(r -> age >= r.minAge && age <= r.maxAge)
            .findFirst()
            .orElse(null);
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
