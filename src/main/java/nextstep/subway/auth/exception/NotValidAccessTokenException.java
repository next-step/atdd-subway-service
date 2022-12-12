package nextstep.subway.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotValidAccessTokenException extends RuntimeException {

    public NotValidAccessTokenException(String message) {
        super(message);
    }
}
