package nextstep.subway.fare.domain.distance.impl;

import nextstep.subway.fare.domain.distance.DistancePolicy;

public class BasicDistancePolicy implements DistancePolicy {

    private BasicDistancePolicy() {
    }

    private static class LazyHolder {
        private static BasicDistancePolicy instance = new BasicDistancePolicy();
    }

    public static BasicDistancePolicy getInstance() {
        return LazyHolder.instance;
    }


    @Override
    public int calculate(int distance) {
        return 0;
    }
}
