package nextstep.subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(SubwayException.class)
    public ResponseEntity handleException(SubwayException e) {
        return new ResponseEntity<>(e.getMessage() , e.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
//        return ResponseEntity.badRequest().build();
//    }

}