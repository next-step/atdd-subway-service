package nextstep.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
