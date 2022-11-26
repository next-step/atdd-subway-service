package nextstep.subway.handler;

import nextstep.subway.exception.EntityNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, EntityNotFound.class})
    public ResponseEntity<Void> handle() {
        return ResponseEntity.badRequest().build();
    }
}
