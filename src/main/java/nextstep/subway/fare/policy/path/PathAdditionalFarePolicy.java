package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.path.domain.Path;

public interface PathAdditionalFarePolicy {
    FarePolicy getPolicy(Path path);
}
