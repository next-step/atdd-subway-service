package nextstep.subway.line.exception;

public class LineException extends RuntimeException {
    public LineException(final LineExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public LineException(final String message) {
        super(message);
    }
}
