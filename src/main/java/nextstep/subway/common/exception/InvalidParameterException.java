package nextstep.subway.common.exception;

/**
 * 유효하지 않은 매개 변수가 메소드에 전달 될 때 발생합니다.
 */
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterException(Throwable cause) {
        super(cause);
    }
}
