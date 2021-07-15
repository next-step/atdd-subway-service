package nextstep.subway.fare;


import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum FareAgeRule {
    TEENAGER(13, 19, 0.8),
    CHILD(6, 13, 0.5);

    private static final long AGE_DISCOUNT_FARE = 350;

    private final int minAge;
    private final int maxAge;
    private final double discountRate;

    FareAgeRule(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static Optional<FareAgeRule> fareByAge(Integer age) {
        return Arrays.stream(FareAgeRule.values())
                .filter(matchAge(age))
                .findFirst();
    }

    private static Predicate<FareAgeRule> matchAge(Integer age) {
        return fareAgeRule -> age != null && fareAgeRule.minAge <= age && fareAgeRule.maxAge > age;
    }

    public static long fareByDiscount(Optional<FareAgeRule> fareAgeRule, long fare) {
        return fareAgeRule
            .map(fareAgeRule2 -> fareAgeRule2.discount(fare))
            .orElse(fare);
    }

    private long discount(long fare) {
        return (long) ((fare - AGE_DISCOUNT_FARE) * getDiscountRate());
    }
}
