package nextstep.subway.line.exception;

public class NotFoundSectionException extends RuntimeException {

    public NotFoundSectionException() {
        super();
    }

    public NotFoundSectionException(String message) {
        super(message);
    }
}
