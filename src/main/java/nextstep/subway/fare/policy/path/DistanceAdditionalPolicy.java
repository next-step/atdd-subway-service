package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;

public class DistanceAdditionalPolicy implements PathAdditionalPolicy {

    private static final int DISTANCE_FARE = 100;

    DistanceAdditionalPolicy() {
    }

    @Override
    public Fare apply(Fare fare, Path path) {
        return fare.add(PayZone.totalPoint(path.distance()) * DISTANCE_FARE);
    }
}
