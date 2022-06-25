package nextstep.subway.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(final AuthorizationExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public AuthorizationException(final String message) {
        super(message);
    }
}
