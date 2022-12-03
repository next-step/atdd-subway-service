package nextstep.subway.path.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;

public interface FarePolicy {
    Fare calculateFare(Distance distance);
}
