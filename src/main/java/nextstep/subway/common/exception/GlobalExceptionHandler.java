package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
    }
}
