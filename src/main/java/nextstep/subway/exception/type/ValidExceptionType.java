package nextstep.subway.exception.type;

public enum ValidExceptionType {
    STATION_BOTH_NOT_EXIST("등록할 수 없는 구간 입니다"),
    ALREADY_EXIST_LINE_STATION("이미 등록된 구간 입니다.");

    private final String message;

    ValidExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}