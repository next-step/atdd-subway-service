package nextstep.subway.member.domain;

import java.util.Arrays;

public enum MemberShip {
    PREFERENTIAL(0, 0, 65),
    GENERAL(0, 1, 19),
    TEENAGER(350, 0.8, 13),
    CHILD(350, 0.5, 6);

    private final int deduction;
    private final double discount;
    private final int minAge;

    MemberShip(int deduction, double discount, int minAge) {
        this.deduction = deduction;
        this.discount = discount;
        this.minAge = minAge;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscount() {
        return discount;
    }

    public int getMinAge() {
        return minAge;
    }

    public static MemberShip findMemberShip(int age) {
        return Arrays.stream(MemberShip.values())
                .filter(value -> value.minAge <= age)
                .findFirst()
                .orElse(MemberShip.PREFERENTIAL);
    }
}
