package nextstep.subway.path.domain.fare;

public interface FareCalculationPolicy {
    int DEFAULT_FARE = 1_250;

    int calculateFare();
}
