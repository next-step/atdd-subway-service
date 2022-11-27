package nextstep.subway.constant;

public enum ErrorCode {
    DISTANCE_BIGGEST_THAN_ZERO("거리는 0보다 커야 합니다."),
    BOTH_STATION_EXIST("이미 등록된 구간 입니다."),
    BOTH_STATION_NOT_EXIST("상행성 하행선 모두 존재하지 않습니다."),
    STATION_IS_NOT_EXIST("삭제하려는 지하철이 노선에 존재하지 않습니다"),
    LAST_SECTION_CAN_NOT_DELETE("마지막 구간은 삭제할 수 없습니다."),
    FIND_PATH_SAME_SOURCE_TARGET("출발역과 도착역이 같습니다."),
    FIND_PATH_NOT_EXIST("최단 경로를 조회하려는 역이 존재하지 않습니다."),
    FIND_PATH_NOT_CONNECT("출발역과 도착역이 연결이 되어 있지 않습니다."),
    MEMBER_NOT_EXIST_BY_EMAIL("해당 이메일로 조회되는 사용자가 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
