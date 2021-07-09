package nextstep.subway.line.domain;


import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.AuthenticationPrincipal;

import javax.persistence.Embeddable;

@Embeddable
public class Fee {
    private static final int DEFAULT_FEE = 1250;

    private int fee;

    protected Fee() {}

    private Fee(int fee) {
        validate(fee);
        this.fee = fee;
    }

    public static Fee of(int fee) {
        return new Fee(fee);
    }

    public static Fee ofWithOverFare(int overFare) {
        return new Fee(DEFAULT_FEE + overFare);
    }

    public Fee calculateBasicFee(Distance distance) {
        DistanceSurchargePolicy surchargePolicy = DistanceSurchargePolicy.of(distance);
        return surchargePolicy.calculate(this, distance);
    }

    public Fee calculateAgeFee(@AuthenticationPrincipal AuthMember loginMember) {
        AgeDiscountPolicy discountPolicy = AgeDiscountPolicy.of(loginMember.getAge());
        return discountPolicy.calculate(this);
    }

    public int get() {
        return fee;
    }

    private void validate(int fee) {
        if (fee < 0) {
            throw new IllegalArgumentException("요금에 대한 금액은 0보다 작을 수 없습니다.");
        }
    }

    private boolean isMoreThanFifty(Distance distance) {
        return distance.get() > 50;
    }

    private boolean isTenToFifty(Distance distance) {
        return 10 < distance.get() && distance.get() <= 50;
    }

    private int calculateFiveOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1d) / 5) + 1) * 100);
    }

    private int calculateEightOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1d) / 8) + 1) * 100);
    }
}