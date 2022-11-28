package nextstep.subway.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Void> handle() {
        return ResponseEntity.badRequest().build();
    }
}
