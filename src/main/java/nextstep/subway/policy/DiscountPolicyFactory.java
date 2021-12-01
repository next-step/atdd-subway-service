package nextstep.subway.policy;

import nextstep.subway.policy.discount.ChildDiscountPolicy;
import nextstep.subway.policy.discount.NoneDiscountPolicy;
import nextstep.subway.policy.discount.DiscountPolicy;
import nextstep.subway.policy.discount.YouthDiscountPolicy;

public class DiscountPolicyFactory {
    private static final int YOUTH_END_AGE = 19;
    private static final int CHILD_END_AGE = 13;
    private static final int CHILD_START_AGE = 6;

    private DiscountPolicyFactory() {
        throw new IllegalAccessError("Factory클래스로 생성자가 허용되지 않습니다.");
    }

    public static DiscountPolicy generate(Integer age) {
        if (age >= CHILD_START_AGE && age < CHILD_END_AGE) {
            return new ChildDiscountPolicy();
        }

        if (age >= CHILD_END_AGE && age < YOUTH_END_AGE) {
            return new YouthDiscountPolicy();
        }

        return new NoneDiscountPolicy();
    }
}
