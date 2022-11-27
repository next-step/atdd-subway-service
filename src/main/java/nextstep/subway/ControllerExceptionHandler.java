package nextstep.subway;

import nextstep.subway.exception.DomainException;
import nextstep.subway.exception.EntityNotFoundException;
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

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Void> handleDomainException() {
        return badRequest();
    }

    private ResponseEntity<Void> badRequest() {
        return ResponseEntity.badRequest().build();
    }
}
