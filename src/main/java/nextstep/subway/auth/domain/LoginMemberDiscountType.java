package nextstep.subway.auth.domain;

public enum LoginMemberDiscountType {
    KID(13, 0.5),
    ADOLESCENT(19, 0.2),
    NONE(0, 0);

    public static final int BASE_DISCOUNT_FEE = 350;
    public static final int MINIMUM_DISCOUNT_AGE = 6;

    private int discountAge;
    private double discountPercentage;

    LoginMemberDiscountType(int discountAge, double discountPercentage) {
        this.discountAge = discountAge;
        this.discountPercentage = discountPercentage;
    }

    public static LoginMemberDiscountType getLoginMemberDiscountType(int age) {
        if (age >= MINIMUM_DISCOUNT_AGE && age < KID.discountAge) {
            return KID;
        }
        if (age >= KID.discountAge && age < ADOLESCENT.discountAge) {
            return ADOLESCENT;
        }

        return NONE;
    }

    public static int calculateMemberDiscountPrice(int price, LoginMemberDiscountType loginMemberDiscountType) {
        return (int) ((price - BASE_DISCOUNT_FEE) * (1.0 - loginMemberDiscountType.discountPercentage));
    }

    public static int getMinimumDiscountAge() {
        return MINIMUM_DISCOUNT_AGE;
    }

    public int getDiscountAge() {
        return discountAge;
    }

    public static int getBaseDiscountFee() {
        return BASE_DISCOUNT_FEE;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
