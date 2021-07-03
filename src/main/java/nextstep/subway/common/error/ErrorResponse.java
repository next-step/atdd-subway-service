package nextstep.subway.common.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private static final String DEFAULT_ERROR_MESSAGE = "예기치 못한 오류가 발생하였습니다.";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private String timestamp;
    private int status;
    private String errorMessage;

    protected ErrorResponse() {
    }

    private ErrorResponse(int statusCode, String timestamp, String errorMessage) {
        this.status = statusCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public static ErrorResponse of(HttpStatus status, String errorMessage) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        return new ErrorResponse(status.value(), timestamp, errorMessage);
    }

    public static ErrorResponse of(HttpStatus status) {
        return ErrorResponse.of(status, DEFAULT_ERROR_MESSAGE);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
