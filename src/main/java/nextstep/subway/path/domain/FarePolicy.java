package nextstep.subway.path.domain;

public interface FarePolicy {
    Fare calculateFare(int distance);
}
