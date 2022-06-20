package nextstep.subway.line.exception;

public enum LineExceptionType {
    LINE_NOT_FOUND("해당 라인이 존재하지 않습니다."),
    EXIST_SECTION("이미 등록된 구간 입니다."),
    CAN_NOT_REGISTER_SECTION("등록할 수 없는 구간 입니다.");
    
    private final String message;

    LineExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
