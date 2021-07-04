package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.path.domain.Path;

public class DistanceAdditionalPolicy extends PathAdditionalPolicy {

    private static final int DISTANCE_FARE = 100;

    DistanceAdditionalPolicy() {
    }

    @Override
    public FarePolicy of(Object object) {
        checkPathObject(object);
        return fare -> fare.add(PayZone.totalPoint(((Path)object).distance()) * DISTANCE_FARE);
    }
}
