package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public class CannotFindPathException extends SubwayCustomException {

    public CannotFindPathException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public CannotFindPathException(HttpStatus status, ExceptionType exceptionType) {
        super(status, exceptionType);
    }

    public CannotFindPathException(ExceptionType exceptionType) {
        super(HttpStatus.BAD_REQUEST, exceptionType);
    }
}
