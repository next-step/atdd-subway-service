package nextstep.subway.path.utils;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PayZone;

public class DistanceFarePolicy implements AdditionalFarePolicy {
    private static final int DISTANCE_FARE = 100;

    @Override
    public int applyPolicy(Path path) {
        return PayZone.totalPoint(path.distance()) * DISTANCE_FARE;
    }
}
