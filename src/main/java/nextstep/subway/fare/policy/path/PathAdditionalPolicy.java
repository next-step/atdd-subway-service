package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;

public interface PathAdditionalPolicy {
    Fare apply(Fare fare, Path path);
}
