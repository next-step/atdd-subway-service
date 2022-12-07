package nextstep.subway;

public enum ErrorMessage {
    //구간오류 메시지
    ALREADY_EXIST_SECTION("이미 등록된 구간 입니다."),
    NO_EXIST_STATIONS_TO_REGISTER("등록할 수 없는 구간 입니다."),
    EXCEED_SECTION_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    INVALID_DISTANCE_VALUE("제한 구간 길이보다 작습니다. 제한 구간 길이 : "),
    INVALID_SECTION("유효하지 않은 구간 입니다."),

    //역오류 메시지
    NO_EXIST_STATIONS_TO_DELETE("등록되지 않은 역은 삭제할 수 없습니다."),
    DO_NOT_DELETE_UNIQUE_SECTION("구간이 하나여서 삭제할 수 없습니다."),

    //길찾기 오류 메시지
    SOURCE_TARGET_EQUAL("출발역과 도착역이 동일 합니다."),
    DISCONNECT_SOURCE_TARGET("출발지에서 도착지로 이동할 수 없습니다."),

    //회원관리 오류 메시지
    DO_NOT_FIND_EMAIL("입력하신 email에 해당하는 아이디가 없습니다."),

    //즐겨찾기 오류 메시지
    DO_NOT_EXIST_FAVORITES_LIST("삭제할 수 있는 즐겨찾기 항목이 없습니다."),

    DO_NOT_EXIST_STATION_ID("존재하지 않는 역 아이디입니다."),
    DO_NOT_EXIST_LINE_ID("존재하지 않는 노선 아이디 입니다."),
    DO_NOT_EXIST_MEMBER_ID("존재하지 않는 회원 아이디입니다."),
    DO_NOT_EXIST_FAVORITES_ID("존재하지 않는 즐겨찾기 아이디입니다.");


    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String setLimitValueAndGetMessage(String limitValue) {
        return message + limitValue;
    }
}
