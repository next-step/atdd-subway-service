package nextstep.subway.exception;

public class InvalidFareException extends RuntimeException {
    public InvalidFareException(String message) {
        super(message);
    }
}
