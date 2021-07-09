package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;

class GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy implements OverFareByDistanceStrategy {
    public static final int PER_KM = 5;
    public static final int OVER_FATE_PER_KM = 100;
    public static final int MIN_DISTANCE = 11;
    public static final int MAX_DISTANCE = 50;

    @Override
    public int calculateOverFare(ShortestDistance distance) {
        int overDistance = distance.value() - NotMoreThan10KmOverFareByDistanceStrategy.MAX_DISTANCE;
        return (int) ((Math.ceil((overDistance - 1) / PER_KM) + 1) * OVER_FATE_PER_KM);
    }

    @Override
    public boolean isAvailable(ShortestDistance distance) {
        return distance.isGraterThan(MIN_DISTANCE - 1) && distance.isNotMoreThan(MAX_DISTANCE);
    }
}
