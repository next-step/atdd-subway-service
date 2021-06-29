package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DefaultLineFarePolicy implements FarePolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
