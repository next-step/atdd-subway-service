package nextstep.subway.exception;

public class LineHasNotExistSectionException extends RuntimeException{
    public LineHasNotExistSectionException() {
    }

    public LineHasNotExistSectionException(String message) {
        super(message);
    }

    public LineHasNotExistSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineHasNotExistSectionException(Throwable cause) {
        super(cause);
    }

    public LineHasNotExistSectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
