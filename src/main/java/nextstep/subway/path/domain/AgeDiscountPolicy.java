package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Member;

public class AgeDiscountPolicy implements DiscountPolicy {
    private final int fare;

    public AgeDiscountPolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculate(Member member) {
        AgeType ageType = AgeType.findByAge(member.getAge());
        return ageType.calculateDiscountFare(fare);
    }
}
