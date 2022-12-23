package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public SubwayException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
