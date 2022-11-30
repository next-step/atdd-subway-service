package nextstep.subway.exception.type;

public enum NotFoundDataExceptionType {
    NOT_FOUND_LINE("존재하지 않는 LINE 이에요");

    private final String message;

    NotFoundDataExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
