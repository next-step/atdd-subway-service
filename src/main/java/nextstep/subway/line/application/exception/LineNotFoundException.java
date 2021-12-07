package nextstep.subway.line.application.exception;

public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException() {
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
