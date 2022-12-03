package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.fare.Fare;

public interface FarePolicy {
    Fare calculateFare(Distance distance);
}
