package nextstep.subway.common;

import nextstep.subway.line.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity pathExceptionHandler(PathException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity memberExceptionHandler(MemberException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity favoriteExceptionHandler(FavoriteException e) {
        return ResponseEntity.badRequest().build();
    }
}
