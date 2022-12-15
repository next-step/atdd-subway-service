package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum DiscountPolicy {
    ADULT(19, 500, 0, 0),
    TEENAGER(13, 19, 350, 0.2),
    CHILD(6, 13, 350, 0.5),
    INFANT(0, 6, 0, 1);

    private int ageStart;
    private final int ageEnd;
    private final int minusFare;
    private final double discountRatio;

    DiscountPolicy(int ageStart, int ageEnd, int minusFare, double discountRatio) {

        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.minusFare = minusFare;
        this.discountRatio = discountRatio;
    }

    public int getDiscount(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException();
        }
        double discounted = (fare - this.minusFare) * (1 - this.discountRatio);
        return (int) discounted;
    }

    public static DiscountPolicy getPolicy(int age) {
        return Arrays.stream(values()).filter(
                policy -> policy.isMatch(age)
            ).findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public int getAgeStart() {
        return ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }

    public boolean isMatch(int age) {
        return getAgeStart() <= age && age < getAgeEnd();
    }
}
