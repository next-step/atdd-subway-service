package nextstep.subway.path.domain;

public class AgeDiscountPolicy {
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double YOUTH_DISCOUNT_PERCENT = 0.8;
    private static final double CHILD_DISCOUNT_PERCENT = 0.5;
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 12;
    private static final int YOUTH_MIN_AGE = 13;
    private static final int YOUTH_MAX_AGE = 18;

    private AgeDiscountPolicy() {
    }

    public static Fare discount(Fare fare, int age) {
        if (isYouth(age)) {
            return fare.minus(DEDUCTIBLE_AMOUNT).apply(YOUTH_DISCOUNT_PERCENT);
        }
        if (isChild(age)) {
            return fare.minus(DEDUCTIBLE_AMOUNT).apply(CHILD_DISCOUNT_PERCENT);
        }
        return fare;
    }

    private static boolean isChild(int age) {
        return age >= CHILD_MIN_AGE && age <= CHILD_MAX_AGE;
    }

    private static boolean isYouth(int age) {
        return age >= YOUTH_MIN_AGE && age <= YOUTH_MAX_AGE;
    }
}
