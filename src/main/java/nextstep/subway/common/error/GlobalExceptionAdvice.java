package nextstep.subway.common.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException() {
        String errorMessage = "예기치 못한 오류가 발생하였습니다.";
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage));
    }
}
