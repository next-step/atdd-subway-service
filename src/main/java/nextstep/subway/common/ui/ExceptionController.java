package nextstep.subway.common.ui;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NoSuchDataException;
import nextstep.subway.path.application.PathService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NoSuchDataException.class, InvalidDataException.class, DataIntegrityViolationException.class})
    public ResponseEntity handleDataIntegrityViolationException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}