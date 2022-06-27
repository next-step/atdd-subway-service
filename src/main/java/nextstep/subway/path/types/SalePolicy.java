package nextstep.subway.path.types;

public enum SalePolicy {
    YOUTH(0.2) {
        @Override
        public int calculateCharge(final int totalCharge) {
            return calculateYouthCharge(totalCharge);
        }
    }, CHILD(0.5) {
        @Override
        public int calculateCharge(final int totalCharge) {
            return calculateChildCharge(totalCharge);
        }
    }, ADULT(0) {
        @Override
        public int calculateCharge(final int totalCharge) {
            return totalCharge;
        }
    };

    private static final Integer YOUTH_MIN_AGE = 13;
    private static final Integer YOUTH_MAX_AGE = 18;
    private static final Integer CHILD_MIN_AGE = 6;
    private static final Integer CHILD_MAX_AGE = 12;

    private static final int DEDUCTION_AMOUNT = 350;

    private double discountRate;

    SalePolicy(final double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static int calculateAgeDiscountFrom(final int totalCharge, final Integer age) {
        if (YOUTH_MIN_AGE <= age && age <= YOUTH_MAX_AGE) {
            return YOUTH.calculateCharge(totalCharge);
        }
        if (CHILD_MIN_AGE <= age && age <= CHILD_MAX_AGE) {
            return CHILD.calculateCharge(totalCharge);
        }
        return ADULT.calculateCharge(totalCharge);
    }

    abstract public int calculateCharge(final int age);

    private static int calculateYouthCharge(final int totalCharge) {
        return (int) Math.round((totalCharge - DEDUCTION_AMOUNT) * (1 - YOUTH.getDiscountRate()));
    }

    private static int calculateChildCharge(final int totalCharge) {
        return (int) Math.round((totalCharge - DEDUCTION_AMOUNT) * (1 - CHILD.getDiscountRate()));
    }
}
