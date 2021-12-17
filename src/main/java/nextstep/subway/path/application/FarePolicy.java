package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;

public interface FarePolicy {
    Fare calculateFare(int distance);
}
