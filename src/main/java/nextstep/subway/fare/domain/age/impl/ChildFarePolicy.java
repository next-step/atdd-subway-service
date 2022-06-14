package nextstep.subway.fare.domain.age.impl;

import nextstep.subway.fare.domain.age.AgePolicy;

public class ChildFarePolicy implements AgePolicy {

    private ChildFarePolicy() {
    }

    private static class LazyHolder {
        private static ChildFarePolicy instnace = new ChildFarePolicy();
    }

    public static ChildFarePolicy getInstance() {
        return LazyHolder.instnace;
    }

    @Override
    public boolean includeAge(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public int calculate() {
        return (int) ((DEFAULT_FARE - DISCOUNT_FARE) * 0.5);
    }

    @Override
    public int discountRate() {
        return 50;
    }
}
