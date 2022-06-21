package nextstep.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    public static final String INVALID_ACCESS_TOKEN_MSG = "유효하지 않은 로그인 토큰 정보입니다.";

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
