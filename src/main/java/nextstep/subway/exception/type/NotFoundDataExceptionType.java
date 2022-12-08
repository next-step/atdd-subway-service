package nextstep.subway.exception.type;

public enum NotFoundDataExceptionType {
    NOT_FOUND_LINE("존재하지 않는 구간 이에요"),
    NOT_FOUND_MEMBER("유저가 존재하지 않습니다"),
    NOT_FOUND_SOURCE_AND_TARGET_STATION("출발역과 종점역이 존재하지 않아요");
    private final String message;

    NotFoundDataExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
