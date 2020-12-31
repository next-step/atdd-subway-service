package nextstep.subway.path.ui;

import nextstep.subway.path.domain.exceptions.PathFindingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PathControllerAdvice {
    @ExceptionHandler(PathFindingException.class)
    public ResponseEntity handlePathFindingException(PathFindingException e) {
        return ResponseEntity.badRequest().build();
    }
}
