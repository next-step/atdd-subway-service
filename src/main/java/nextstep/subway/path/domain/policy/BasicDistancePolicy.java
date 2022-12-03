package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.ExtraFare;

public class BasicDistancePolicy implements DistancePolicy {

    @Override
    public int calculate() {
        return ExtraFare.BASIC;
    }
}
