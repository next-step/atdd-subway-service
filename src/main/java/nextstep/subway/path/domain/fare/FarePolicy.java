package nextstep.subway.path.domain.fare;

public interface FarePolicy<E> {
    Fare calculateFare(E element);
}
