package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DefaultDistancePolicy implements FarePolicy {
    @Override
    public int calculate(int fare) {
        return fare;
    }
}
