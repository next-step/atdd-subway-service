package nextstep.subway.line.domain;

public interface FarePolicy {
    Fare calculateFare(Distance distance);
}
