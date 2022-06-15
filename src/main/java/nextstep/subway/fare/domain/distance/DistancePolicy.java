package nextstep.subway.fare.domain.distance;

public interface DistancePolicy {
    boolean includeDistance(int distance);
    int calculate(int distance);
}
