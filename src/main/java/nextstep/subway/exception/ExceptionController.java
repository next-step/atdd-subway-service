package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({DataIntegrityViolationException.class, BadRequestException.class, PersistenceException.class})
    public ResponseEntity<?> handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
