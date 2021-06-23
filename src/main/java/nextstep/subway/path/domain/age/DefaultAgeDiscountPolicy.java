package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.wrapped.Money;

import static java.lang.String.format;

public class DefaultAgeDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public Money calcFare(LoginMember member, Money money) {
        if (!isSupport(member)) {
            throw new IllegalArgumentException(
                    format("나이는 %d세 미만 %d세 초과여야 합니다", ChildDiscountPolicy.MINIMUM_AGE, TeenagerDiscountPolicy.MAXIMUM_AGE));
        }

        return money;
    }

    @Override
    public boolean isSupport(LoginMember member) {
        return member.getAge() < ChildDiscountPolicy.MINIMUM_AGE || member.getAge() > TeenagerDiscountPolicy.MAXIMUM_AGE;
    }
}
