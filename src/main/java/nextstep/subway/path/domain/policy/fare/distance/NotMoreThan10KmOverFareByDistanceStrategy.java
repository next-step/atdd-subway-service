package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;

public class NotMoreThan10KmOverFareByDistanceStrategy implements OverFareByDistanceStrategy {
    public static final int MAX_DISTANCE = 10;

    @Override
    public int calculateOverFare(ShortestDistance distance) {
        return 0;
    }

    @Override
    public boolean isAvailable(ShortestDistance distance) {
        return distance.isNotMoreThan(MAX_DISTANCE);
    }
}
