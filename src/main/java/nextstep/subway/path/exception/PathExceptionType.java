package nextstep.subway.path.exception;

public enum PathExceptionType {
    EQUALS_STATION("출발역과 도착지가 같습니다."),
    NO_PATH("출발역과 도착역이 연결이 되어 있지 않은 경우");

    private final String message;

    PathExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
