package nextstep.subway.exception.domain;

public enum SubwayExceptionMessage {
    DUPLICATE_SECTION("이미 등록된 구간 입니다."),
    NOT_REGISTER_SECTION("등록할 수 없는 구간 입니다."),
    NOT_REMOVE_SECTION("구간을 삭제 할 수 없습니다."),
    OVER_THE_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    ;

    private final String message;

    SubwayExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
