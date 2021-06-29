package nextstep.subway.exception;

public class NoRouteException extends RuntimeException {
    public NoRouteException() {
    }

    public NoRouteException(String message) {
        super(message);
    }

    public NoRouteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRouteException(Throwable cause) {
        super(cause);
    }

    public NoRouteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
