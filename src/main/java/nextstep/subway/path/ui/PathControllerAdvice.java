package nextstep.subway.path.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.path.PathException;

@RestControllerAdvice
public class PathControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> noValidatedInputException(DataIntegrityViolationException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<ErrorResponse> invalidDistanceException(PathException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
