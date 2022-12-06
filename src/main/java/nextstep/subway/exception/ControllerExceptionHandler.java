package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<DomainException> handleDomainException(Exception e) {
        return new ResponseEntity<>(new DomainException(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<AuthorizationException> handleAuthorizationException(Exception e) {
        return new ResponseEntity<>(new AuthorizationException(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Void> badRequest() {
        return ResponseEntity.badRequest().build();
    }
}
