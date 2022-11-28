package nextstep.subway.line.exception;

public class StationException extends RuntimeException {
    public StationException(final StationExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public StationException(final String message) {
        super(message);
    }
}
