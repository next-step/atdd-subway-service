package nextstep.subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity IllegalArgumentExceptionHandler() {
        return ResponseEntity.badRequest().build();
    }
}
