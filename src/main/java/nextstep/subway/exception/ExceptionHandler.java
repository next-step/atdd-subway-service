package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundLoginMemberException.class)
    public ResponseEntity handleNotFoundLoginMemberException(NotFoundLoginMemberException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundFavoritesException.class)
    public ResponseEntity handleNotFoundFavoritesException(NotFoundFavoritesException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity handleNotFoundMemberException(NotFoundMemberException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPathSearchingException.class)
    public ResponseEntity handleInvalidPathSearchingException(InvalidPathSearchingException e) {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotRemovableException.class)
    public ResponseEntity handleNotRemovableException(NotRemovableException e) {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidSectionException.class)
    public ResponseEntity handleInvalidSectionException(InvalidSectionException e) {
        return ResponseEntity.badRequest().build();
    }
  
}
