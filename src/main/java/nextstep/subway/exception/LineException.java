package nextstep.subway.exception;

public class LineException extends RuntimeException {
    public LineException() {
        super();
    }

    public LineException(String message) {
        super(message);
    }
}
