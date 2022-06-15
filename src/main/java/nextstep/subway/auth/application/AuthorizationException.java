package nextstep.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super("인증 정보가 올바르지 않습니다.");
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
