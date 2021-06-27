package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final CustomExceptionMessage exceptionMessage;

    public CustomException(final CustomExceptionMessage message) {
        super(message.getMessage());
        this.exceptionMessage = message;
    }

    public HttpStatus getStatus() {
        return this.exceptionMessage.getStatus();
    }
}
