package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public interface FarePolicy {
    Fare calculateFare(Distance distance);
}
