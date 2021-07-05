package nextstep.subway.path.infra;

import nextstep.subway.path.domain.RatePolicy;

public class RatePolicyByAddition implements RatePolicy {
    private final int addition;

    public RatePolicyByAddition(final int addition) {
        this.addition = addition;
    }

    @Override
    public double calc(double fee) {
        return fee + addition;
    }
}
