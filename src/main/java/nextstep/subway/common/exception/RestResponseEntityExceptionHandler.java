package nextstep.subway.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DatabaseException.class)
    protected ResponseEntity<ErrorResponse> handleDatabaseExceptionConflict(DatabaseException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(value = InvalidParameterException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterExceptionConflict(
        InvalidParameterException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleLineNameDuplicateConflict() {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(ErrorCode.DATABASE_CONSTRAINT_VIOLATION));
    }
}
