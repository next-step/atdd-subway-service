package nextstep.subway.path.application;

import nextstep.subway.path.domain.fare.Fare;

public interface FarePolicy {
    Fare calculateFare(int distance);
}
