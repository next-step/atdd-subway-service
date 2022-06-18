package nextstep.subway;

import nextstep.subway.path.exceptions.SourceAndTargetSameException;
import nextstep.subway.path.exceptions.SourceNotConnectedWithTargetException;
import nextstep.subway.path.exceptions.StationNotExistException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DataIntegrityViolationException.class, SourceAndTargetSameException.class,
            SourceNotConnectedWithTargetException.class,
            StationNotExistException.class})
    public ResponseEntity handleIllegalArgsException(final Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
