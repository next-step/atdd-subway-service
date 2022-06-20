package nextstep.subway.common;

import nextstep.subway.path.exception.SameSourceAndTargetException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SameSourceAndTargetException.class)
    public ErrorResponse handleSameSourceAndTargetException() {
        return ErrorResponse.of(ErrorMessage.SAME_SOURCE_AND_TARGET.getMessage());
    }
}
