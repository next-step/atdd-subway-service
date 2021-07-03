package nextstep.subway.fare.policy.path;

import nextstep.subway.path.domain.Path;

public interface AdditionalFarePolicy {
    int applyPolicy(Path path);
}
