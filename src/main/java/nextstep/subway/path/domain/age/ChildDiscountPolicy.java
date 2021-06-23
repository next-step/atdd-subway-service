package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import static java.lang.String.format;

public class ChildDiscountPolicy implements AgeDiscountPolicy {
    public static final int MINIMUM_AGE = 6;
    public static final int MAXIMUM_AGE = 12;

    @Override
    public Money calcFare(LoginMember member, Money money) {
        return null;
    }

    @Override
    public boolean isSupport(LoginMember member) {
        return member.getAge() >= MINIMUM_AGE && member.getAge() <= MAXIMUM_AGE;
    }
}
