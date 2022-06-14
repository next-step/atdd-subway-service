package nextstep.subway.member.constant;

public enum MemberFarePolicy {
    CHILD(0.5, 350), TEENAGER(0.2, 350), GENERAL(0, 0);


    private final double discountPercent;
    private final int deductionAmount;

    MemberFarePolicy(double discountPercent, int deductionAmount) {
        this.discountPercent = discountPercent;
        this.deductionAmount = deductionAmount;
    }

    public static MemberFarePolicy convert(Integer age) {
        if (isTeenager(age)) {
            return TEENAGER;
        }
        if (isChild(age)) {
            return CHILD;
        }
        return GENERAL;
    }

    private static boolean isTeenager(Integer age) {
        return age >= 13 && age < 19;
    }

    private static boolean isChild(Integer age) {
        return age >= 6 && age < 13;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public int getDeductionAmount() {
        return deductionAmount;
    }
}
