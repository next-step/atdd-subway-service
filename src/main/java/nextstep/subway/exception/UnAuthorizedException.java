package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends SubwayCustomException {

    public UnAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnAuthorizedException(ExceptionType exceptionType) {
        super(HttpStatus.UNAUTHORIZED, exceptionType);
    }
}
