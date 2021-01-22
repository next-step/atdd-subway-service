package nextstep.subway.path.domain.fare;

public enum AgeDiscount {
    NONE(1), CHILD(0.5), TEEN(0.8);
    private static final int BASIC_DISCOUNT_FARE = 350;
    private static final Integer SIX_AGE = 6;
    private static final Integer THIRTEEN_AGE = 13;
    private static final Integer NINETEEN_AGE = 19;

    private final double discountRatio;

    AgeDiscount(double discountRatio) {
        this.discountRatio = discountRatio;
    }

    public static AgeDiscount findAgeDiscount(Integer age) {
        if (isChild(age)) {
            return AgeDiscount.CHILD;
        }
        if (isTeen(age)) {
            return AgeDiscount.TEEN;
        }
        return AgeDiscount.NONE;
    }

    private static boolean isChild(Integer age) {
        return SIX_AGE <= age && age < THIRTEEN_AGE;
    }

    private static boolean isTeen(Integer age) {
        return THIRTEEN_AGE <= age && age < NINETEEN_AGE;
    }

    public int discountFare(int fare) {
        if (this.equals(AgeDiscount.NONE)) {
            return fare;
        }
        return (int) ((fare - BASIC_DISCOUNT_FARE) * discountRatio);
    }
}
