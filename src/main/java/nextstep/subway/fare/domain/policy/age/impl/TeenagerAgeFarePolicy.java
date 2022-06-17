package nextstep.subway.fare.domain.policy.age.impl;

import static nextstep.subway.fare.domain.policy.age.AgeFarePolicyType.AGE_DISCOUNT_FARE;
import static nextstep.subway.fare.domain.policy.age.AgeFarePolicyType.DEFAULT_FARE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class TeenagerAgeFarePolicy implements AgeFarePolicy {

    private final int discountRate = 20;

    private static class InstanceHolder {
        private static final TeenagerAgeFarePolicy instance = new TeenagerAgeFarePolicy();
    }

    public static TeenagerAgeFarePolicy getInstance() {
        return InstanceHolder.instance;
    }


    @Override
    public int calculate() {
        return (int) ((DEFAULT_FARE - AGE_DISCOUNT_FARE) * (100 - discountRate) / 100);
    }

    @Override
    public boolean includeAge(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public int getDiscountRate() {
        return discountRate;
    }

}
