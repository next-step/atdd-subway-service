package nextstep.exception;

public enum ErrorCode {

    STATION_NOT_FOUND("해당 역을 찾을 수 없습니다."),
    STATION_NOT_CONNECTED("출발역과 도착역이 연결되지 않았습니다."),
    SAME_SOURCE_AND_TARGET("출발역과 도착역이 같을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
