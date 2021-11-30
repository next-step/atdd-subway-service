package nextstep.subway.exception;

public enum ExceptionMessage {
    LESS_THAN_DISTANCE_BETWEEN_STATION("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    ALREADY_ADD_SECTION("이미 등록된 구간 입니다."),
    NOT_POSSIBLE_ADD_SECTION("등록할 수 없는 구간 입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
