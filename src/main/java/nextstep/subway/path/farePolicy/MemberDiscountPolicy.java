package nextstep.subway.path.farePolicy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;

public interface MemberDiscountPolicy {
    Fare applyDiscount(Fare fare);

    static MemberDiscountPolicy getPolicy(LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return new NoneDiscountPolicy();
        }

        int age = loginMember.getAge();

        if (KidsDiscountPolicy.MIN_AGE <= age && age < KidsDiscountPolicy.MAX_AGE) {
            return new KidsDiscountPolicy();
        }
        if (TeenagersDiscountPolicy.MIN_AGE <= age && age < TeenagersDiscountPolicy.MAX_AGE) {
            return new TeenagersDiscountPolicy();
        }

        return new NoneDiscountPolicy();
    }
}
