package nextstep.subway.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InValidAccessTokenException extends RuntimeException {

    public InValidAccessTokenException(String message) {
        super(message);
    }
}
