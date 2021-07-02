package nextstep.subway.path.utils;

import nextstep.subway.path.domain.Path;

public interface AdditionalFarePolicy {
    int applyPolicy(Path path);
}
