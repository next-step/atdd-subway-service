package nextstep.subway.fare.domain.policy.age.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class FreeAgeFarePolicy implements AgeFarePolicy {

    private final int discountRate = 100;

    private static class InstanceHolder {
        private static final FreeAgeFarePolicy instance = new FreeAgeFarePolicy();
    }

    public static FreeAgeFarePolicy getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public int calculate() {
        return 0;
    }

    @Override
    public boolean includeAge(int age) {
        return age < 6 || age >= 65;
    }

    @Override
    public boolean isFreeAge() {
        return true;
    }

    @Override
    public int getDiscountRate() {
        return discountRate;
    }

}
