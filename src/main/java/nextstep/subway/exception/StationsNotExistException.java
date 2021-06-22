package nextstep.subway.exception;

public class StationsNotExistException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "요청한 역들이 존재하지 않습니다.";

    public StationsNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public StationsNotExistException(String message) {
        super(message);
    }
}
