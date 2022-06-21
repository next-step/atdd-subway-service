package nextstep.subway.fare.domain;

import nextstep.subway.member.domain.MemberShip;

public class AgeDiscount implements DiscountPolicy {

    private final MemberShip memberShip;

    public AgeDiscount(int age) {
        this.memberShip = MemberShip.findMemberShip(age);
    }

    @Override
    public int discountFare(int fare) {
        return (int) ((fare - memberShip.getDeduction()) * memberShip.getDiscount());
    }
}
