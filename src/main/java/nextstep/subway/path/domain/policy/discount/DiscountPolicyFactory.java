package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DiscountPolicyFactory {
    public static final int BASIC_DISCOUNT_STANDARD = 350;

    public static FarePolicy findPolicy(int age) {
        if (age >= 13 && age < 19) {
            return new TeenagerDiscountPolicy();
        }

        if (age >= 6 && age < 13) {
            return new ChildrenDiscountPolicy();
        }
        return new DefaultDiscountPolicy();
    }
}
