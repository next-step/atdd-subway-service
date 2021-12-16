package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Money;

import java.util.Arrays;

public enum DiscountAge {

    BABY(1, 5, 0.0f, 0),
    CHILD(6, 12, 0.5f, 350),
    YOUTH(13, 18, 0.8f, 350),
    BASIC(19, 64, 1.0f, 0),
    ELDER(65, 200, 0.0f, 0);

    private final int minAge;
    private final int maxAge;
    private final float rate;
    private final int deduction;

    DiscountAge(int minAge, int maxAge, float rate, int deduction) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.rate = rate;
        this.deduction = deduction;
    }

    public static DiscountAge findBy(int age) {
        return Arrays.stream(values())
                .filter(it -> (age >= it.minAge) && (age <= it.maxAge))
                .findFirst()
                .orElse(BASIC);
    }

    public float getRate() {
        return rate;
    }

    public int getDeduction() {
        return deduction;
    }
}
