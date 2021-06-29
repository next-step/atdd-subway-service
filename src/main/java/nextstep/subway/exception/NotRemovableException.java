package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotRemovableException extends RuntimeException {
    public NotRemovableException() {
        super();
    }

    public NotRemovableException(String message) {
        super(message);
    }
}
