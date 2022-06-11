package nextstep.subway.path.domain.exception;

public class NotExistPathException extends Exception {
    public NotExistPathException() {
    }

    public NotExistPathException(String message) {
        super(message);
    }

    public NotExistPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
