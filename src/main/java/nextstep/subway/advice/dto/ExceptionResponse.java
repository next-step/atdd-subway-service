package nextstep.subway.advice.dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String message;

    public ExceptionResponse() {}

    private ExceptionResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ExceptionResponse of(HttpStatus httpStatus, String message) {
        return new ExceptionResponse(httpStatus, message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
