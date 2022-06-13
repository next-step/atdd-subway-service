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
    public int calculate() {
        return (int) ((DEFAULT_FARE - DISCOUNT_FARE) * 0.8);
    }
}
