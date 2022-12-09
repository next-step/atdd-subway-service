package nextstep.subway.global.advice;

import nextstep.subway.global.message.GlobalAdviceMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String BODY_KEY_MESSAGE = "message";

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put(BODY_KEY_MESSAGE, GlobalAdviceMessage.NOT_FOUND_ENTITY.message());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> body = new HashMap<>();
        body.put(BODY_KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put(BODY_KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}
