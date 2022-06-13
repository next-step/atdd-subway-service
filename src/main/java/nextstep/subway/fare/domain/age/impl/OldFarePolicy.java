package nextstep.subway.fare.domain.age.impl;

import nextstep.subway.fare.domain.age.AgePolicy;

public class OldFarePolicy implements AgePolicy {
    private OldFarePolicy() {
    }

    private static class LazyHolder {
        private static OldFarePolicy instance = new OldFarePolicy();
    }

    public static OldFarePolicy getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public int calculate() {
        return 0;
    }
}
