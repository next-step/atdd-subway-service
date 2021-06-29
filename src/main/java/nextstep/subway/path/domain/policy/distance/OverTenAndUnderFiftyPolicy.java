package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

public class OverTenAndUnderFiftyPolicy implements FarePolicy {
    private final int distance;

    public OverTenAndUnderFiftyPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
