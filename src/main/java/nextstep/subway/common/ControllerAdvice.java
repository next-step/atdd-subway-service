package nextstep.subway.common;

import nextstep.subway.exception.CanNotFoundStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(CanNotFoundStationException.class)
    public ResponseEntity handleCanNotFoundStationException(CanNotFoundStationException e) {
        return ResponseEntity.badRequest().build();
    }
}
