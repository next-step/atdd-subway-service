package nextstep.subway.common;

public enum ErrorCode {

    // 노선 [01xx]
    NOT_FOUND_LINE_ID("0101", "존재하지 않는 노선ID 입니다.")
    , NOT_REGISTERED_STATION_TO_LINE("0102", "노선에 등록되지 않은 역입니다.")

    // 구간 [02xx]
    , DUPLICATE_SECTION("0201", "이미 등록된 구간 입니다.")
    , CAN_NOT_ADD_SECTION("0202", "등록할 수 없는 구간 입니다.")
    , CAN_NOT_REMOVE_SECTION("0203", "삭제할 수 있는 구간이 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
