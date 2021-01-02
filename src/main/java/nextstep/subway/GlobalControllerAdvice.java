package nextstep.subway;

import nextstep.subway.auth.application.exceptions.InvalidLoginTryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(InvalidLoginTryException.class)
    public ResponseEntity handleInvalidLoginTryException(InvalidLoginTryException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
