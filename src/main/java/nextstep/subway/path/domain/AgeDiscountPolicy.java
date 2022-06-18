package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class AgeDiscountPolicy implements DiscountPolicy {
    @Override
    public int calculate(int fare, LoginMember loginMember) {
        AgeType ageType = AgeType.findByAge(loginMember.getAge());
        return ageType.calculateDiscountFare(fare);
    }
}
