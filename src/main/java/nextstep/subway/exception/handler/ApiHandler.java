package nextstep.subway.exception.handler;

import nextstep.subway.exception.PathFindException;
import nextstep.subway.exception.SectionManagerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiHandler {
    @ExceptionHandler
    public ResponseEntity PathFindExceptionHandler(PathFindException e){
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity SectionManagerExceptionHandler(SectionManagerException e){
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity IllegalArgumentExceptionHandler(IllegalArgumentException e){
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }
}
