package nextstep.subway.fare.domain.policy.age.impl;

import static nextstep.subway.fare.domain.policy.age.AgeFarePolicyType.AGE_DISCOUNT_FARE;
import static nextstep.subway.fare.domain.policy.age.AgeFarePolicyType.DEFAULT_FARE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class ChildAgeFarePolicy implements AgeFarePolicy {

    private final int discountRate = 50;

    private static class InstanceHolder {
        private static final ChildAgeFarePolicy instance = new ChildAgeFarePolicy();
    }

    public static ChildAgeFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate() {
        return (int) ((DEFAULT_FARE - AGE_DISCOUNT_FARE) * (100 - discountRate) / 100);
    }

    @Override
    public boolean includeAge(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public int getDiscountRate() {
        return discountRate;
    }

}
