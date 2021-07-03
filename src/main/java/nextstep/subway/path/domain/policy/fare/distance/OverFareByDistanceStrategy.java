package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;

public interface OverFareByDistanceStrategy {
    int calculateOverFare(ShortestDistance distance);
    boolean isAvailable(ShortestDistance distance);
}
