package nextstep.subway.auth.application.exception;

public class AuthorizationException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "로그인 실패";

    public AuthorizationException() {
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
