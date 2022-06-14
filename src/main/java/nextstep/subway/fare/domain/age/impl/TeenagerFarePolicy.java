package nextstep.subway.fare.domain.age.impl;

import nextstep.subway.fare.domain.age.AgePolicy;

public class TeenagerFarePolicy implements AgePolicy {

    private TeenagerFarePolicy() {
    }

    private static class LazyHolder {
        private static TeenagerFarePolicy instance = new TeenagerFarePolicy();
    }

    public static TeenagerFarePolicy getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public boolean includeAge(int age) {
        return age >= 13 && age <= 18;
    }

    @Override
    public int calculate() {
        return (int) ((DEFAULT_FARE - DISCOUNT_FARE) * 0.8);
    }

    @Override
    public int discountRate() {
        return 20;
    }
}
