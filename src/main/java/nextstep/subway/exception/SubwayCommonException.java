package nextstep.subway.exception;

public class SubwayCommonException extends RuntimeException {

    public SubwayCommonException() {
    }

    public SubwayCommonException(String message) {
        super(message);
    }

    public SubwayCommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubwayCommonException(Throwable cause) {
        super(cause);
    }

    public SubwayCommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
