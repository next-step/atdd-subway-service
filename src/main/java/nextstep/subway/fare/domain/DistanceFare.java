package nextstep.subway.fare.domain;

public class DistanceFare {
    private int distance;
    private DistancePolicy policy;

    public DistanceFare(int distance,DistancePolicy policy){
        this.distance = distance;
        this.policy = policy;
    }

    public int getFare(int fare){
        return fare + policy.getFare(distance);
    }
}
