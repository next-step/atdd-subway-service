package nextstep.subway.exception;

public class LineHasNotExistStationException extends RuntimeException{
    public LineHasNotExistStationException() {
    }

    public LineHasNotExistStationException(String message) {
        super(message);
    }

    public LineHasNotExistStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineHasNotExistStationException(Throwable cause) {
        super(cause);
    }

    public LineHasNotExistStationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
