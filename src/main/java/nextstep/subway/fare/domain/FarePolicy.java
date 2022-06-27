package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public interface FarePolicy {
    Fare calculate(Distance distance);

}
