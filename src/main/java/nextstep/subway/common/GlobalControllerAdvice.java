package nextstep.subway.common;

import nextstep.subway.line.application.exception.InvalidSectionException;
import nextstep.subway.line.application.exception.LineNotFoundException;
import nextstep.subway.line.application.exception.SectionNotFoundException;
import nextstep.subway.path.application.exception.InvalidPathException;
import nextstep.subway.station.application.exception.StationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handler(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity handler(StationNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity handler(SectionNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity handler(LineNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidSectionException.class)
    public ResponseEntity handler(InvalidSectionException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity handler(InvalidPathException e) {
        return ResponseEntity.badRequest().build();
    }
}
