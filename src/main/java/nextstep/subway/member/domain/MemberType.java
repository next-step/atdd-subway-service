package nextstep.subway.member.domain;

import java.util.Arrays;

public enum MemberType {
    CHILD(350, 0.5, 6, 13),
    TEENAGER(350, 0.2, 13, 19),
    LOGIN(0, 0, 0, 0);


    double deductedAmount;
    double discountRate;
    int lowerBoundaryAge;
    int upperBoundaryAge;

    MemberType(double deductedAmount, double discountRate, int lowerBoundaryAge, int upperBoundaryAge) {
        this.deductedAmount = deductedAmount;
        this.discountRate = discountRate;
        this.lowerBoundaryAge = lowerBoundaryAge;
        this.upperBoundaryAge = upperBoundaryAge;
    }

    public int getLowerBoundaryAge() {
        return lowerBoundaryAge;
    }

    public int getUpperBoundaryAge() {
        return upperBoundaryAge;
    }

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
        MemberType matchType = Arrays.stream(values())
            .filter(memberType -> hasMatchMemberType(memberType, age))
            .findAny().orElse(LOGIN);

        return matchType;
    }

    private static boolean hasMatchMemberType(MemberType memberType, int age) {
        return memberType.getLowerBoundaryAge() <= age && age < memberType.getUpperBoundaryAge();
    }
}
