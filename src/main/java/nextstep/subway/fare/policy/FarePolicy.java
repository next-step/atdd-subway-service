package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;

@FunctionalInterface
public interface FarePolicy {
    Fare calculate(Fare fare);
}
