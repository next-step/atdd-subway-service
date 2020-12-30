package nextstep.subway.line.ui;

import nextstep.subway.station.application.exceptions.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LineControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotfoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
