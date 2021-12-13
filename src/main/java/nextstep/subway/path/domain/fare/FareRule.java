package nextstep.subway.path.domain.fare;

public abstract class FareRule {
    private static final int BASE_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final DistancePolicy distancePolicy = new DistancePolicy();

    public int distanceFare(int distance) {
        int overDistance = distancePolicy.getOverDistance(distance);
        return BASE_FARE + (overDistance * EXTRA_FARE);
    }
}
