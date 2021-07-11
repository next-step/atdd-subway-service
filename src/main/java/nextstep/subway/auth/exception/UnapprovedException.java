package nextstep.subway.auth.exception;

public class UnapprovedException extends IllegalStateException {
    public UnapprovedException(String message) {
        super(message);
    }

    public UnapprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}
