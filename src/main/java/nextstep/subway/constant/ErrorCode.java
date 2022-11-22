package nextstep.subway.constant;

public enum ErrorCode {
    DISTANCE_BIGGEST_THAN_ZERO("거리는 0보다 커야 합니다."),
    BOTH_STATION_EXIST("이미 등록된 구간 입니다."),
    BOTH_STATION_NOT_EXIST("상행성 하행선 모두 존재하지 않습니다."),
    STATION_IS_NOT_EXIST("삭제하려는 지하철이 노선에 존재하지 않습니다"),
    LAST_SECTION_CAN_NOT_DELETE("마지막 구간은 삭제할 수 없습니다."),
    ENTITY_NOT_FOUND_BY_ID("Id에 해당하는 엔티티를 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
