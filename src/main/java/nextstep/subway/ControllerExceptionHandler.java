package nextstep.subway;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.SectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(LineException.class)
    public ResponseEntity lineExceptionHandler(LineException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity sectionExceptionHandler(SectionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
