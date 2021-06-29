package nextstep.subway.exception;

public class NotExistMinimumFareLine extends RuntimeException {
    public NotExistMinimumFareLine() {
    }

    public NotExistMinimumFareLine(String message) {
        super(message);
    }

    public NotExistMinimumFareLine(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistMinimumFareLine(Throwable cause) {
        super(cause);
    }

    public NotExistMinimumFareLine(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
