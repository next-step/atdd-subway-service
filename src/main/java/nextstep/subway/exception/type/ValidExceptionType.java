package nextstep.subway.exception.type;

public enum ValidExceptionType {
    STATION_BOTH_NOT_EXIST("등록할 수 없는 구간 입니다"),
    ALREADY_EXIST_LINE_STATION("이미 등록된 구간 입니다."),
    NOT_ZERO_VALID_DISTANCE("거리는 0보다 커야합니다"),
    NOT_SHORT_VALID_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    SECTIONS_MIN_SIZE_ONE("구간의 사이즈는 1보다 커야합니다"),
    IS_TARGET_ANS_SOURCE_SAME("시작역과 출발역이 같습니다."),
    MIN_EXTRA_FEE_NOT_ZERO("추가 요금은 0원보다 작을 수 없습니다."),
    MIN_AGE_NOT_ZERO("나이는 0살 보다 작을 수 없습니다."),
    NOT_CONNECT_STATION("시작역과 출발역이 연결되어있지 않습니다.");

    private final String message;

    ValidExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}