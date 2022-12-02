package nextstep.subway.auth.application;

import nextstep.subway.common.exception.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super(ErrorEnum.NOT_EXISTS_EMAIL.message());
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
