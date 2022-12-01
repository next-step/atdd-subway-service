package nextstep.subway.common.exception.handler;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.SubwayException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({SubwayException.class})
    public ResponseEntity SubwayException(SubwayException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity AuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
