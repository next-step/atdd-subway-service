package nextstep.subway;

import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.CannotFindException;
import nextstep.subway.exception.DataAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<Void> handleDataAlreadyExists(DataAlreadyExistsException e) {
        return ResponseEntity.status(500).build();
    }

    @ExceptionHandler(CannotAddException.class)
    public ResponseEntity<Void> handleCannotAdd(CannotAddException e) {
        return ResponseEntity.status(500).build();
    }

    @ExceptionHandler(CannotFindException.class)
    public ResponseEntity<Void> handleCannotFind(CannotFindException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CannotDeleteException.class)
    public ResponseEntity<Void> handleCannotDelete(CannotDeleteException e) {
        return ResponseEntity.status(500).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(500).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void>  handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}
