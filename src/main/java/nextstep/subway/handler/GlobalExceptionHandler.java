package nextstep.subway.handler;

import nextstep.subway.exception.EntityNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<Void> handleEntityNotFoundException() {
        return ResponseEntity.badRequest().build();
    }
}
