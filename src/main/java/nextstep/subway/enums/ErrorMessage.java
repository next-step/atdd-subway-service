package nextstep.subway.enums;

public enum ErrorMessage {
    DUPLCATED_SECTION("이미 등록된 구간 입니다."),
    NOT_EXIST_SECTION("등록할 수 없는 구간 입니다."),
    EXCEEDED_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    EMPTY_STATIONS("등록된 역이 없습니다."),
    DUPLICATED_STATION_PATH("출발역과 도착역이 같을 경우 최단 거리를 조회할 수 없습니다.");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
