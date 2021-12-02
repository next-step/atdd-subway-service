package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    STATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 역을 찾을 수 없습니다."),
    STATION_NOT_CONNECTED(HttpStatus.BAD_REQUEST, "출발역과 도착역이 연결되지 않았습니다."),
    SAME_SOURCE_AND_TARGET(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
