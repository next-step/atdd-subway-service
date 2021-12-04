package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity IllegalArgumentExceptionHandler(Exception e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
