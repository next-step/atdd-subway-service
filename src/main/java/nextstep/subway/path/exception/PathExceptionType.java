package nextstep.subway.path.exception;

public enum PathExceptionType {
    EQUALS_STATION("출발역과 도착지가 같습니다.");

    private final String message;

    PathExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
