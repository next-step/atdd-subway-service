package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalArgumentException extends RuntimeException {
    private String message;

    public IllegalArgumentException(String message) {
        super(message);
        this.message = message;
    }

    public IllegalArgumentException(ErrorMessage message) {
        super(message.getMessage());
        this.message = message.getMessage();
    }
}
