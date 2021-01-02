package nextstep.subway.station.ui;

import nextstep.subway.line.domain.exceptions.InvalidAddSectionException;
import nextstep.subway.line.domain.exceptions.InvalidDistanceValueException;
import nextstep.subway.line.domain.exceptions.InvalidRemoveSectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StationControllerAdvice {
    @ExceptionHandler(InvalidRemoveSectionException.class)
    public ResponseEntity handleInvalidRemoveSectionException(InvalidRemoveSectionException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidAddSectionException.class)
    public ResponseEntity handleInvalidAddSectionException(InvalidAddSectionException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidDistanceValueException.class)
    public ResponseEntity handleInvalidDistanceValueException(InvalidDistanceValueException e) {
        return ResponseEntity.badRequest().build();
    }
}
