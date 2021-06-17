package nextstep.subway.exception;

public class LineHasNotExistShortestException extends RuntimeException {
    public LineHasNotExistShortestException() {
    }

    public LineHasNotExistShortestException(String message) {
        super(message);
    }

    public LineHasNotExistShortestException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineHasNotExistShortestException(Throwable cause) {
        super(cause);
    }

    public LineHasNotExistShortestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
