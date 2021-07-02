package nextstep.subway.path.utils;

import nextstep.subway.path.domain.Path;

public class BasicFarePolicy implements AdditionalFarePolicy {
    private static final int BASIC_FARE = 1250;

    @Override
    public int applyPolicy(Path path) {
        return BASIC_FARE;
    }
}
