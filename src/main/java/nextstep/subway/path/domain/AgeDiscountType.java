package nextstep.subway.path.domain;

public enum AgeDiscountType {
    KID(13, 0.5),
    ADOLESCENT(19, 0.2),
    NONE(0, 0);

    public static final int BASE_DISCOUNT_FEE = 350;
    public static final int MINIMUM_DISCOUNT_AGE = 6;

    private final int discountAge;
    private final double discountPercentage;

    AgeDiscountType(int discountAge, double discountPercentage) {
        this.discountAge = discountAge;
        this.discountPercentage = discountPercentage;
    }

    public static AgeDiscountType getAgeDiscountType(int age) {
        if (age >= MINIMUM_DISCOUNT_AGE && age < KID.discountAge) {
            return KID;
        }
        if (age >= KID.discountAge && age < ADOLESCENT.discountAge) {
            return ADOLESCENT;
        }

        return NONE;
    }

    public static int getDiscountedPrice(int price, AgeDiscountType ageDiscountType) {
        if (ageDiscountType == AgeDiscountType.NONE) {
            return price;
        }

        return (int) ((price - BASE_DISCOUNT_FEE) * (1.0 - ageDiscountType.discountPercentage));
    }
}
