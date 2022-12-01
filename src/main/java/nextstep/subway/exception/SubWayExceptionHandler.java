package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubWayExceptionHandler {

    @ExceptionHandler(
            {DataIntegrityViolationException.class, NotFoundDataException.class, NotValidDataException.class})
    protected ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}