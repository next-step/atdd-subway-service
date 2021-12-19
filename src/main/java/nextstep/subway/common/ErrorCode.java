package nextstep.subway.common;

public enum ErrorCode {

    // 노선 [01xx]
    NOT_FOUND_LINE_ID("0101", "존재하지 않는 노선ID 입니다.")
    , NOT_REGISTERED_STATION_TO_LINE("0102", "노선에 등록되지 않은 역입니다.")

    // 구간 [02xx]
    , DUPLICATE_SECTION("0201", "이미 등록된 구간 입니다.")
    , CAN_NOT_ADD_SECTION("0202", "등록할 수 없는 구간 입니다.")
    , CAN_NOT_REMOVE_SECTION("0203", "삭제할 수 있는 구간이 없습니다.")
    , INVALID_SECTION_DISTANCE("0204", "역과 역 사이의 거리보다 좁은 거리를 입력해주세요")

    // 역 [03xx]
    , NOT_FOUND_STATION_ID("0301", "존재하지 않는 역ID 입니다.")

    // 경로 [04xx]
    , INVALID_PATH("0401", "출발역과 도착역은 같을 수 없습니다.")
    , UNCOUPLED_PATH("0402", "출발역과 도착역이 연결이 되어 있지 않습니다.")

    // 회원 [05xx]
    , NOT_FOUND_MEMBER_ID("0501", "존재하지 않는 회원ID 입니다.")
    , INVALID_MEMBER_INFO("0502", "유효하지 않은 회원 정보입니다.")
    , INVALID_TOKEN("0503", "유효하지 않은 토큰입니다.")
    , NO_PERMISSION("0504", "권한이 없습니다.")

    // 즐겨찾기 [06xx]
    , NOT_FOUND_FAVORITE_ID("0601", "존재하지 않는 즐겨찾기ID 입니다.");

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
