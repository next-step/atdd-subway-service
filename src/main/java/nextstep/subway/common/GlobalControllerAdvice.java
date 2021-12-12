package nextstep.subway.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handler(DataIntegrityViolationException e) {
        ApiError apiError = new ApiError(BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handler(NotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ApiError> handler(InvalidException e) {
        ApiError apiError = new ApiError(BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
