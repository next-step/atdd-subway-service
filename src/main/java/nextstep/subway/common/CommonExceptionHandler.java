package nextstep.subway.common;

import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import nextstep.subway.common.exception.*;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest()
            .build();
    }

    @ExceptionHandler(CannotAddException.class)
    public ResponseEntity handleCannotAddException(CannotAddException e) {
        return ResponseEntity.badRequest()
            .build();
    }

    @ExceptionHandler(CannotRemoveException.class)
    public ResponseEntity handleCannotRemoveException(CannotRemoveException e) {
        return ResponseEntity.badRequest()
            .build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound()
            .build();
    }
}
