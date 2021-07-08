package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;

public class DiscountPolicyFactory {
    public static DiscountPolicy getDiscountPolicy(final LoginMember loginMember) {
        if (loginMember == null || loginMember.getAge() == null) {
            return new NoneDiscountPolicy();
        }

        if (loginMember.isTeenager()) {
            return new TeenagerDiscountPolicy();
        }

        if (loginMember.isChildren()) {
            return new ChildrenDiscountPolicy();
        }

        return new NoneDiscountPolicy();
    }
}
