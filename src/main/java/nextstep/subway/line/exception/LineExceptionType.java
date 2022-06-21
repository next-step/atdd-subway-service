package nextstep.subway.line.exception;

public enum LineExceptionType {
    LINE_NOT_FOUND("해당 라인이 존재하지 않습니다."),
    EXIST_SECTION("이미 등록된 구간 입니다."),
    CAN_NOT_REGISTER_SECTION("등록할 수 없는 구간 입니다."),
    MIN_SECTION_DELETION("최소 구간은 삭제할수 없습니다."),
    DISTANCE_MINUS_ERROR("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    DISTANCE_MIN_ERROR("역과의 거리는 0보다 커야 합니다.");


    private final String message;

    LineExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
