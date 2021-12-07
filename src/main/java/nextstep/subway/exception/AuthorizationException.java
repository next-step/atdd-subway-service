package nextstep.subway.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
}
