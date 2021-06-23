package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.wrapped.Money;

public class DefaultAgeDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public Money calcFare(LoginMember member, Money money) {
        return null;
    }

    @Override
    public boolean isSupport(LoginMember member) {
        return member.getAge() < ChildDiscountPolicy.MINIMUM_AGE || member.getAge() > TeenagerDiscountPolicy.MAXIMUM_AGE;
    }
}
