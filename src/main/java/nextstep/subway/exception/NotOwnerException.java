package nextstep.subway.exception;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException() {
        super();
    }

    public NotOwnerException(String message) {
        super(message);
    }
}
