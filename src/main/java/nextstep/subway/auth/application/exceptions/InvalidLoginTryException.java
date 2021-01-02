package nextstep.subway.auth.application.exceptions;

public class InvalidLoginTryException extends RuntimeException {
    public InvalidLoginTryException(final String message) {
        super(message);
    }
}
