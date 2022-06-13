package nextstep.subway.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorCodeHandler {
    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity handleError(ErrorCodeException e) {
        return ResponseEntity.badRequest().build();
    }
}
