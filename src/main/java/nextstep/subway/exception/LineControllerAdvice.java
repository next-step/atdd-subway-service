package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.line.LineAlreadyExistException;
import nextstep.subway.exception.line.NotFoundSectionException;
import nextstep.subway.exception.line.NotFoundStationsException;

@RestControllerAdvice
public class LineControllerAdvice extends ControllerAdvice {

    @ExceptionHandler(LineAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> lineAlreadyExistException(LineAlreadyExistException e) {
        return getSeverErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundSectionException.class)
    public ResponseEntity<ErrorResponse> notFoundLineException(NotFoundSectionException e) {
        return getSeverErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundStationsException.class)
    public ResponseEntity<ErrorResponse> notFoundStationsException(NotFoundStationsException e) {
        return getSeverErrorResponse(e.getMessage());
    }

}
