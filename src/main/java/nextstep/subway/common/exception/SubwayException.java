package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public SubwayException(HttpStatus status, ErrorMessage message) {
        this.status = status;
        this.message = message.getMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
