package nextstep.subway.enums;

public enum ErrorMessage {
    DUPLCATED_SECTION("이미 등록된 구간 입니다."),
    NOT_EXIST_SECTION("등록할 수 없는 구간 입니다."),
    EXCEEDED_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    EMPTY_STATIONS("등록된 역이 없습니다."),
    DUPLICATED_STATION_PATH("출발역과 도착역이 같을 경우 최단 거리를 조회할 수 없습니다."),
    UNCONNECTED_STATION_PATH("출발역과 도착역이 연결되지 않았습니다."),
    NOT_EXISTS_STATION_PATH("출발역 또는 도착역이 존재하지 않습니다."),
    SAME_START_END_STATION("출발역과 도착역이 같습니다."),
    EXIST_FAVORITE("이미 존재하는 즐겨찾기입니다."),
    NOT_EXIST_FAVORITE("존재하지 않는 즐겨찾기입니다."),
    NOT_EQUALS_MEMBER("즐겨찾기들의 회원은 동일해야 합니다."),
    NOT_EXIST_MEMBER("존재하지 않는 회원입니다."),
    INVALID_STRING_FORMAT("문자열로 변경할 수 없는 포맷 입니다."), NOT_SAME_FAVORITE_MEMBER("자신의 즐겨찾기여야 합니다.");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
