package nextstep.subway.path.fare.policy.extra;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public interface ExtraFarePolicyStrategy {
    Fare calculate(Path path);
}
