package nextstep.subway.fare.domain;

import nextstep.subway.member.domain.MemberShip;

public class DiscountByAge implements DiscountPolicy {

    private final MemberShip memberShip;

    public DiscountByAge(int age) {
        this.memberShip = MemberShip.findMemberShip(age);
    }

    @Override
    public int discountFare(int fare) {
        return (int) ((fare - memberShip.getDeduction()) * memberShip.getDiscount());
    }
}
