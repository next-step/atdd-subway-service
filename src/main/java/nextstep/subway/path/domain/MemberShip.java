package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.math.BigDecimal;
import java.util.Arrays;

public enum MemberShip {

    NONE(1, BigDecimal.valueOf(0), member -> !member.isYouth() && !member.isChild()),
    YOUTH(0.8, BigDecimal.valueOf(350), member -> member.isYouth()),
    CHILD(0.5, BigDecimal.valueOf(350), member -> member.isChild());

    private final double discountPercent;
    private final BigDecimal baseDiscount;
    private final MemberShipStrategy strategy;

    MemberShip(double discountPercent, BigDecimal baseDiscount, MemberShipStrategy strategy) {
        this.discountPercent = discountPercent;
        this.baseDiscount = baseDiscount;
        this.strategy = strategy;
    }

    public static BigDecimal memberShipCalculate(LoginMember loginMember, BigDecimal fare) {
        MemberShip memberShip = Arrays.stream(MemberShip.values())
                .filter(it -> it.strategy.isMemberShipConfirm(loginMember))
                .findFirst()
                .orElse(NONE);

        return calculate(memberShip, fare);
    }

    private static BigDecimal calculate(MemberShip memberShip, BigDecimal fare) {
        fare = fare.subtract(memberShip.getBaseDiscount());
        fare = fare.multiply(BigDecimal.valueOf(memberShip.getDiscountPercent()));
        return fare;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getBaseDiscount() {
        return baseDiscount;
    }
}
