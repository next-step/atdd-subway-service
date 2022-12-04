package nextstep.subway;

public enum ErrorMessage {
    ALREADY_EXIST_SECTION("이미 등록된 구간 입니다."),
    NO_EXIST_STATIONS_TO_REGISTER("등록할 수 없는 구간 입니다."),
    EXCEED_SECTION_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    INVALID_DISTANCE_VALUE("제한 구간 길이보다 작습니다. 제한 구간 길이 : "),
    NO_EXIST_STATIONS_TO_DELETE("등록되지 않은 역은 삭제할 수 없습니다."),
    DO_NOT_DELETE_UNIQUE_SECTION("구간이 하나여서 삭제할 수 없습니다."),
    DO_NOT_EXIST_STATION_IN_LINE("노선에 존재하지 않는 역입니다."),
    INVALID_STATION_ID("존재하지 않는 역 id 입니다.");

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
