package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public enum ErrorCode implements ErrorCodeMapperType{

    DEFAULT_ERROR(INTERNAL_SERVER_ERROR, "internal_error"),
    BAD_ARGUMENT(BAD_REQUEST, "bad argument"),
    NOT_FOUND_ENTITY(BAD_REQUEST, "not found entity"),
    ALREADY_EXIST_ENTITY(INTERNAL_SERVER_ERROR, "already exist entity");

    private final HttpStatus httpStatus;
    private final int statusCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.statusCode = httpStatus.value();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatusCode() {
        return httpStatus.value();
    }
}
