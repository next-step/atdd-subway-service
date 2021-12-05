package nextstep.subway.exception;

public enum ExceptionMessage {
    LESS_THAN_DISTANCE_BETWEEN_STATION("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    ALREADY_ADD_SECTION("이미 등록된 구간 입니다."),
    NOT_POSSIBLE_ADD_SECTION("등록할 수 없는 구간 입니다."),
    NOT_REMOVE_SECTION_MIN_SIZE("구간이 하나인 경우 구간을 제거할 수 없습니다."),
    NON_EXIST_STATION_TO_SECTION("해당 역이 존재하는 구간이 없습니다."),
    NOT_FOUND_DATA("데이터를 찾을 수 없습니다."),
    SAME_STATION("출발역과 도착역이 같습니다."),
    NOT_EXIST_STATION("해당역이 존재하지 않습니다");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
