package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidSurchargeException extends RuntimeException {
    public InvalidSurchargeException(String message) {
        super(message);
    }
}
