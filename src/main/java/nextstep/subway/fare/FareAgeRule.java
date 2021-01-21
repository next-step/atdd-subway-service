package nextstep.subway.fare;


import java.util.Arrays;
import java.util.function.Predicate;

public enum FareAgeRule {
    청소년(13, 19, 0.8),
    어린이(6, 13, 0.5);

    private static final long AGE_DISCOUNT_FARE = 350;
    private static final double DEFAULT_DISCOUNT_RATE = 1;

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

    public static long discountFareByAge(Integer age, long fare) {
        double rate = Arrays.stream(FareAgeRule.values())
                        .filter(validateAge(age))
                        .map(FareAgeRule::getDiscountRate)
                        .findFirst().orElse(DEFAULT_DISCOUNT_RATE);
        if (rate == DEFAULT_DISCOUNT_RATE) return fare;

        return (long) ((fare - AGE_DISCOUNT_FARE) * rate);
    }

    private static Predicate<FareAgeRule> validateAge(Integer age) {
        return fareAgeRule -> age != null && fareAgeRule.minAge <= age && fareAgeRule.maxAge > age;
    }
}
