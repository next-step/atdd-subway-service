package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private int statusCode;
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public ErrorResponse() {
    }

    public static ErrorResponse build() {
        return new ErrorResponse();
    }

    public ErrorResponse httpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
