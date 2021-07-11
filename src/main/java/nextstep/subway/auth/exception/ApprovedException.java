package nextstep.subway.auth.exception;

public class ApprovedException extends IllegalStateException {
    public ApprovedException(String message) {
        super(message);
    }

    public ApprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}
