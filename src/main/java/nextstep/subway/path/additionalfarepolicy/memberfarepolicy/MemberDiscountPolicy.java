package nextstep.subway.path.additionalfarepolicy.memberfarepolicy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface MemberDiscountPolicy {
    Fare applyDiscount(Fare fare);
    boolean isAvailable(int age);

    static MemberDiscountPolicy getPolicy(LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return new NoneDiscountPolicy();
        }

        int age = loginMember.getAge();
        List<MemberDiscountPolicy> policies = new ArrayList<>(Arrays.asList(
                new KidsDiscountPolicy()
                , new TeenagersDiscountPolicy()
        ));

        return policies.stream()
                .filter(memberDiscountPolicy -> memberDiscountPolicy.isAvailable(age))
                .findAny()
                .orElse(new NoneDiscountPolicy());
    }
}
