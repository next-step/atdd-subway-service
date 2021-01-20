package nextstep.subway.fare;


import java.util.Arrays;
import java.util.function.Predicate;

public enum FareAgeRule {
    청소년(13, 19, 0.8),
    어린이(8, 13, 0.5);

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

    public static long discountFareByAge(int age, long fare) {
        double rate = Arrays.stream(FareAgeRule.values())
                        .filter(validateAge(age))
                        .map(FareAgeRule::getDiscountRate)
                        .findFirst().orElseThrow(() -> new RuntimeException());
        return (long) ((fare - AGE_DISCOUNT_FARE) * rate);
    }

    private static Predicate<FareAgeRule> validateAge(int age) {
        return fareAgeRule -> fareAgeRule.minAge <= age && fareAgeRule.maxAge > age;
    }
}
