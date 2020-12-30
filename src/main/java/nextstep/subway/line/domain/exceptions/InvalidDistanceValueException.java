package nextstep.subway.line.domain.exceptions;

public class InvalidDistanceValueException extends RuntimeException {
    public InvalidDistanceValueException(final String message) {
        super(message);
    }
}
