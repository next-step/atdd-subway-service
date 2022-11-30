package nextstep.subway.path.ui;

import nextstep.subway.line.exception.InvalidPathException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PathControllerAdvice {

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<String> invalidPathException(InvalidPathException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
