package nextstep.subway.common.exception;

public enum ErrorCode {
    SECTION_EXIST("이미 등록된 구간 입니다."),
    SECTION_ADD_NO_POSITION("등록할 수 없는 구간 입니다."),
    DISTANCE_RANGE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    SECTION_ONE_COUNT_CAN_NOT_REMOVE("구간이 하나 일 경우 제거 할 수 없습니다."),
    NOT_EMPTY("빈 값을 입력 할 수 없습니다."),
    STATION_NAME_DUPLICATE_DATA("지하철 역 이름이 이미 존재합니다"),
    LINE_NAME_DUPLICATE_DATA("노선 이름이 이미 존재합니다."),
    SECTION_NOT_FOUND("지하철 역을 찾을 수 없습니다."),
    LINE_NOT_FOUND("노선을 찾을 수 없습니다."),
    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
