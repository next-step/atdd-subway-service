package nextstep.subway.fare.domain.distance.impl;

import nextstep.subway.fare.domain.distance.DistancePolicy;

/**
 * 10km 이상 50km 이하의 거리에 대한 추가 요금 정책
 * */
public class MiddleDistancePolicy implements DistancePolicy{

    private MiddleDistancePolicy() {
    }

    private static class LazyHolder {
        private static MiddleDistancePolicy instance = new MiddleDistancePolicy();
    }

    public static MiddleDistancePolicy getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public int calculate(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
