package nextstep.subway.handler;

import nextstep.subway.auth.application.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorizationExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
