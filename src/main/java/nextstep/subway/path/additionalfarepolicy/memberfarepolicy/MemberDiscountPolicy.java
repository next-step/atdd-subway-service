package nextstep.subway.path.additionalfarepolicy.memberfarepolicy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;

public interface MemberDiscountPolicy {
    Fare applyDiscount(Fare fare);

    static MemberDiscountPolicy getPolicy(LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return new NoneDiscountPolicy();
        }

        int age = loginMember.getAge();

        if (KidsDiscountPolicy.isAvailable(age)) {
            return new KidsDiscountPolicy();
        }
        if (TeenagersDiscountPolicy.isAvailable(age)) {
            return new TeenagersDiscountPolicy();
        }

        return new NoneDiscountPolicy();
    }
}
