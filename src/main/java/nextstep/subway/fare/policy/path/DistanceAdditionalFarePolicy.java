package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.path.domain.Path;

public class DistanceAdditionalFarePolicy implements PathAdditionalFarePolicy {

    private static final int DISTANCE_FARE = 100;

    @Override
    public FarePolicy getPolicy(Path path) {
        return fare -> fare.add(PayZone.totalPointOf(path.distance()) * DISTANCE_FARE);
    }
}
