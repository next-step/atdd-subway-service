package nextstep.subway.fare.domain.distance.impl;

import nextstep.subway.fare.domain.distance.DistancePolicy;

/**
 * 50km 초과한 거리에 대한 추가 요금 정책
 * */
public class LongDistancePolicy implements DistancePolicy {

    private LongDistancePolicy() {
    }

    private static class LazyHolder {
        private static LongDistancePolicy instance = new LongDistancePolicy();
    }

    public static LongDistancePolicy getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public boolean includeDistance(int distance) {
        return distance >= 50;
    }

    @Override
    public int calculate(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
