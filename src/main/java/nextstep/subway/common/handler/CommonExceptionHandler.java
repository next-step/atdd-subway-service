package nextstep.subway.common.handler;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RestController
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity handleInvalidDataException(InvalidDataException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity handleNoResultException(NoResultException e) {
        return ResponseEntity.badRequest().build();
    }

}
