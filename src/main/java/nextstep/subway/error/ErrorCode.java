package nextstep.subway.error;

public enum ErrorCode {
    EXISTS_BOTH_STATIONS("이미 등록된 구간 입니다."),
    NO_EXISTS_BOTH_STATIONS("등록할 수 없는 구간 입니다."),
    TOO_LONG_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    NO_EXISTS_STATION("존재하지 않는 역입니다"),
    SOURCE_EQUALS_TARGET("출발역과 도착역이 같습니다"),
    SOURCE_NOT_CONNECT_TARGET("출발역과 도착역이 연결되어 있지 않습니다"),
    CANNOT_DELETE_LAST_SECTION("마지막 구간은 삭제할 수 없습니다"),
    CANNOT_FOUND_USER("사용자가 없습니다");

    ErrorCode(String message) {
        this.message = message;
    }

    String message;
}
