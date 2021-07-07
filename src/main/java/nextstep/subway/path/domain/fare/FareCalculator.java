package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public interface FareCalculator {
    int calculate(Fare fare);
}
