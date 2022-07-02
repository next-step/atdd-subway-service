package nextstep.subway.member.domain;

public enum MemberType {
    GUEST(0, 0), CHILD(350, 0.5), TEENAGER(350, 0.2), LOGIN(0,0);


    double deductedAmount;
    double discountRate;

    MemberType(double deductedAmount, double discountRate) {
        this.deductedAmount = deductedAmount;
        this.discountRate = discountRate;
    }

    public double getDeductedAmount() {
        return deductedAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static MemberType findType(int age) {
        if (6 <= age && age < 13) {
            return CHILD;
        }
        if (13 <= age && age < 19) {
            return TEENAGER;
        }
        return LOGIN;
    }
}
