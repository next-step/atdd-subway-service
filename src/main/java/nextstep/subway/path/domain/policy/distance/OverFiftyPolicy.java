package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

public class OverFiftyPolicy implements FarePolicy {
    private final int distance;

    public OverFiftyPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
