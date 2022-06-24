package nextstep.subway.path.util;

public enum SalePolicy {
    YOUTH(0.2), CHILD(0.5), ADULT(0);

    private final static Integer YOUTH_MIN_AGE = 13;
    private final static Integer YOUTH_MAX_AGE = 18;
    private final static Integer CHILD_MIN_AGE = 6;
    private final static Integer CHILD_MAX_AGE = 12;

    private double discountRate;

    SalePolicy(final double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static SalePolicy getPolicyByAge(final Integer age) {
        if (YOUTH_MIN_AGE <= age && age <= YOUTH_MAX_AGE) {
            return YOUTH;
        }
        if (CHILD_MIN_AGE <= age && age <= CHILD_MAX_AGE) {
            return CHILD;
        }
        return ADULT;
    }
}
