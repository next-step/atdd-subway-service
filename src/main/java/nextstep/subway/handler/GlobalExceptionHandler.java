package nextstep.subway.handler;

import nextstep.subway.auth.application.AuthorizationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity unauthorizedHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity noResultException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
