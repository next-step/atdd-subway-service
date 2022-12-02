package nextstep.subway;

public enum ErrorMessage {
    ALREADY_EXIST_SECTION("이미 등록된 구간 입니다."),
    NO_EXIST_STATIONS("등록할 수 없는 구간 입니다."),
    EXCEED_SECTION_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    INVALID_DISTANCE_VALUE("제한 구간 길이보다 작습니다. 제한 구간 길이 : ");

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
