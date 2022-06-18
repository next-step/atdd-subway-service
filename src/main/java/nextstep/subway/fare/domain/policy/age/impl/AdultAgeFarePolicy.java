package nextstep.subway.fare.domain.policy.age.impl;

import static nextstep.subway.fare.domain.policy.age.AgeFarePolicyType.DEFAULT_FARE;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class AdultAgeFarePolicy implements AgeFarePolicy {

    private final int discountRate = 0;

    private static class InstanceHolder {
        private static final AdultAgeFarePolicy instance = new AdultAgeFarePolicy();
    }

    private AdultAgeFarePolicy() {
    }

    public static AdultAgeFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate() {
        return DEFAULT_FARE;
    }

    @Override
    public boolean includeAge(int age) {
        return age >= 19 && age < 65;
    }

    @Override
    public int getDiscountRate() {
        return discountRate;
    }

}
