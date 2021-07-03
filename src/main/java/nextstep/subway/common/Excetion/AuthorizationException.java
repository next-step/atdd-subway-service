package nextstep.subway.common.Excetion;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("인증되지 않은 계정입니다.");
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
