package nextstep.subway.common.exception.handler;

import nextstep.subway.common.exception.SubwayException;
import org.springframework.dao.DataIntegrityViolationException;
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

}
