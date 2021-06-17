package nextstep.subway.exception;

public class StationNotExistException extends RuntimeException{
    public StationNotExistException() {
    }

    public StationNotExistException(String message) {
        super(message);
    }

    public StationNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public StationNotExistException(Throwable cause) {
        super(cause);
    }

    public StationNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
