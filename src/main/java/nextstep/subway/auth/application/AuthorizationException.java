package nextstep.subway.auth.application;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends BusinessException {
    public AuthorizationException() {
        super(ErrorCode.AUTHORIZATION_FAILED);
    }

    public AuthorizationException(String message) {
        super(message, ErrorCode.AUTHORIZATION_FAILED);
    }
}
