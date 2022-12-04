package nextstep.subway.path.fare.policy;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public interface FarePolicy {
    Fare calculate(Path path);
}
