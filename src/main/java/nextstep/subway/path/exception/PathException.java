package nextstep.subway.path.exception;

public class PathException extends RuntimeException {
    public PathException(final PathExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public PathException(final String message) {
        super(message);
    }
}

