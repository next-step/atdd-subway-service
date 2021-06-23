package nextstep.subway.path.domain.distance_policy;

public abstract class DistanceFarePolicy {

    protected int distance;
    protected static final int BASIC_FARE = 1250;

    public DistanceFarePolicy() {
    }

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    public int calculateByDistance() {
        return BASIC_FARE;
    }
}
