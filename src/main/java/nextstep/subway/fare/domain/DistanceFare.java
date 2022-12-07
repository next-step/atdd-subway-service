package nextstep.subway.fare.domain;

public class DistanceFare {
    private static final int BASE_FARE = 1250;
    private int distance;
    private DistancePolicy policy;

    public DistanceFare(int distance){
        this.distance = distance;
        this.policy = DistancePolicy.valueOfRange(distance);
    }

    public int getFare(int fare){
        return fare + policy.getFare(distance) + BASE_FARE;
    }
}
