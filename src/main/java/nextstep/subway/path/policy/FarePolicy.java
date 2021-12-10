package nextstep.subway.path.policy;

public interface FarePolicy {
    int calculateFare(int extraFare, int distance);
}
