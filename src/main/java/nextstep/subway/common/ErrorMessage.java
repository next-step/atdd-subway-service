package nextstep.subway.common;

public enum ErrorMessage {
    SAME_SOURCE_AND_TARGET("존재하지 않는 노선입니다."),
    STATION_NOT_FOUND("구간에 존재하지 않는 역입니다."),
    NOT_CONNECTED("연결되지 않은 역입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
