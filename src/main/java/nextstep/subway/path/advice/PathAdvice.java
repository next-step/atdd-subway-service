package nextstep.subway.path.advice;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.path.ui.PathController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {PathController.class})
public class PathAdvice {

    @ExceptionHandler(LineException.class)
    public ResponseEntity<Void> lineException(LineException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> lineException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
