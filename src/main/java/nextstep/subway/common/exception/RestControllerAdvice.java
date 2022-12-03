package nextstep.subway.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> IllegalStateException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgsException(DataIntegrityViolationException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorEnum.CREATE_FAIL.message());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorEnum.ERROR_MESSAGE_DEFAULT.message());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
