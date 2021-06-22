package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.path.PathException;

@RestControllerAdvice
public class PathControllerAdvice extends ControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> noValidatedInputException(DataIntegrityViolationException e) {
        return getSeverErrorResponse(e.getMessage());
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<ErrorResponse> invalidDistanceException(PathException e) {
        return getSeverErrorResponse(e.getMessage());
    }

}
