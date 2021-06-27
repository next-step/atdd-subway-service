package nextstep.subway.exception.auth;

public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1097273603599075427L;
    private static final String NO_AUTHORIZATION = "권한이 없습니다.";

    public AuthorizationException() {
        super(NO_AUTHORIZATION);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
