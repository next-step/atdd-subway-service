package nextstep.subway;

import nextstep.subway.exception.PathNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException() {
        return badRequest();
    }

    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity<Void> handlePathNotFoundException() {
        return badRequest();
    }

    private ResponseEntity<Void> badRequest() {
        return ResponseEntity.badRequest().build();
    }
}
