package nextstep.subway.line.exception;

public class DuplicateSectionException extends RuntimeException {

    public DuplicateSectionException() {
        super();
    }

    public DuplicateSectionException(String message) {
        super(message);
    }
}
