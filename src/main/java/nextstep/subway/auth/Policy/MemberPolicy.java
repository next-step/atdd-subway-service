package nextstep.subway.auth.Policy;

import java.util.Arrays;

public enum MemberPolicy {
    TEENAGER_MEMBER(13, 19, 900, 0.8),
    CHILD_MEMBER(6, 13, 900, 0.5),
    BASIC_MEMBER(0, 0, 1250, 1);

    private int minAge;
    private int maxAge;
    private int subwayAmount;
    private double discountRate;

    MemberPolicy(int minAge, int maxAge, int subwayAmount, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.subwayAmount = subwayAmount;
        this.discountRate = discountRate;
    }

    public static MemberPolicy getAgePolicy(int age) {
        return Arrays.stream(MemberPolicy.values())
                .filter(memberPolicy -> memberPolicy.isMatchingAge(age))
                .findFirst()
                .orElse(BASIC_MEMBER);
    }

    public boolean isMatchingAge(int age) {
        return minAge < age && age <= maxAge;
    }

    public boolean isChild() {
        return this == TEENAGER_MEMBER;
    }

    public boolean isTeenager() {
        return this == CHILD_MEMBER;
    }

    public int getMemberCharge() {
        return this.subwayAmount;
    }

    public double getDiscountRate() {
        return this.discountRate;
    }
}
