package nextstep.subway.fare.domain.age.impl;

import nextstep.subway.fare.domain.age.AgePolicy;

public class ToddlerFarePolicy implements AgePolicy {
    private ToddlerFarePolicy() {
    }

    private static class LazyHolder {
        private static ToddlerFarePolicy instnace = new ToddlerFarePolicy();
    }

    public static ToddlerFarePolicy getInstance() {
        return LazyHolder.instnace;
    }

    @Override
    public boolean includeAge(int age) {
        return 6 > age;
    }

    @Override
    public int calculate() {
        return 0;
    }
}
