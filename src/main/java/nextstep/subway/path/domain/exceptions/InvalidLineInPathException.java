package nextstep.subway.path.domain.exceptions;

public class InvalidLineInPathException extends RuntimeException {
    public InvalidLineInPathException(final String message) {
        super(message);
    }
}
