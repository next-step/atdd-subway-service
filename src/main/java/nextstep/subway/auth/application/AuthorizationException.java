package nextstep.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {

    private String errorMessage;
    private AuthorizationErrorCode authorizationErrorCode;

    public AuthorizationException() {
    }

    public AuthorizationException(AuthorizationErrorCode authorizationErrorCode) {
        this(authorizationErrorCode, authorizationErrorCode.errorMessage());
        this.authorizationErrorCode = authorizationErrorCode;
    }

    public AuthorizationException(AuthorizationErrorCode authorizationErrorCode, String errorMessage) {
        super(errorMessage);
        this.authorizationErrorCode = authorizationErrorCode;
        this.errorMessage = errorMessage;
    }
}
