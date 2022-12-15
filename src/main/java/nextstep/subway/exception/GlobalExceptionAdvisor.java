package nextstep.subway.exception;

import nextstep.subway.auth.application.AuthorizationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvisor {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleAuthorizationException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}
