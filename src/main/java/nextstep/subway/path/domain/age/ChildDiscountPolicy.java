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

    private static final Money ABSOLUTE_MINUS_MONEY = new Money(350);

    private static final Money DIVIDE_MONEY = new Money(10);
    private static final Money MULTIPLE_MONEY = new Money(5);

    @Override
    public Money calcFare(LoginMember member, Money money) {
        if (!isSupport(member)) {
            throw new IllegalArgumentException(
                    format("나이는 %d세 이상 %d세 이하여야 합니다", MINIMUM_AGE, MAXIMUM_AGE));
        }

        return money.minus(ABSOLUTE_MINUS_MONEY)
                .divide(DIVIDE_MONEY)
                .multi(MULTIPLE_MONEY);
    }

    @Override
    public boolean isSupport(LoginMember member) {
        return member.getAge() >= MINIMUM_AGE && member.getAge() <= MAXIMUM_AGE;
    }
}
