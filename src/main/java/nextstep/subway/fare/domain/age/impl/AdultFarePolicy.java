package nextstep.subway.fare.domain.age.impl;

import nextstep.subway.fare.domain.age.AgePolicy;

public class AdultFarePolicy implements AgePolicy {
    private AdultFarePolicy() {
    }

    private static class LazyHolder {
        private static AdultFarePolicy instnace = new AdultFarePolicy();
    }

    public static AdultFarePolicy getInstance() {
        return LazyHolder.instnace;
    }

    @Override
    public int calculate() {
        return DEFAULT_FARE;
    }
}
