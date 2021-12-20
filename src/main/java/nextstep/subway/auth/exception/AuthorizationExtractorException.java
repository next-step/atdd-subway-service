package nextstep.subway.auth.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorizationExtractorException extends NoSuchElementException {
    public AuthorizationExtractorException() {
    }

    public AuthorizationExtractorException(String message) {
        super(message);
    }
}
