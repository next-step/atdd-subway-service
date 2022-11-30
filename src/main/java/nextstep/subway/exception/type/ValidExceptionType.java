package nextstep.subway.exception.type;

public enum ValidExceptionType {
    STATION_BOTH_NOT_EXIST("등록할 수 없는 구간 입니다"),
    ALREADY_EXIST_LINE_STATION("이미 등록된 구간 입니다."),
    NOT_ZERO_VALID_DISTANCE("거리는 0보다 커야합니다"),
    NOT_SHORT_VALID_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    SECTIONS_MIN_SIZE_ONE("구간의 사이즈는 1보다 커야합니다");

    private final String message;

    ValidExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}