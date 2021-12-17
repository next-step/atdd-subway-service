package nextstep.subway.common;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private HttpStatus status;

    protected CustomException() {
        super();
    }

    protected CustomException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    protected CustomException(final String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
