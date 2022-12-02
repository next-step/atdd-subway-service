package nextstep.subway.fare;

public class NegativeDistanceException extends RuntimeException {
    public NegativeDistanceException(String message) {
        super(message);
    }
}
