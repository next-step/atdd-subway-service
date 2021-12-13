package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

@FunctionalInterface
public interface DistanceFarePolicy {
    Fare calculate(int distance);
}
