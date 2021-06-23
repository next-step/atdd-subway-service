package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.wrapped.Money;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AgeCalculator {
    private static final List<AgeDiscountPolicy> ageDiscountPolicies = Collections.unmodifiableList(
            Arrays.asList(
                    new DefaultAgeDiscountPolicy(),
                    new ChildDiscountPolicy(),
                    new TeenagerDiscountPolicy()
            )
    );

    public static Money calcAge(LoginMember loginMember, Money money) {
        for (AgeDiscountPolicy discountPolicy : ageDiscountPolicies) {
            money = calcFareIfSupported(discountPolicy, loginMember, money);
        }

        return money;
    }

    private static Money calcFareIfSupported(AgeDiscountPolicy discountPolicy, LoginMember loginMember, Money money) {
        if (discountPolicy.isSupport(loginMember)) {
            money = discountPolicy.calcFare(loginMember, money);
        }
        return money;
    }

}
