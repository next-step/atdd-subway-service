package nextstep.subway.exception.handler;

import nextstep.subway.exception.PathFindException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiHandler {
    @ExceptionHandler
    public ResponseEntity PathFindExceptionHandler(PathFindException e){
        return ResponseEntity.badRequest()
            .build();
    }
}
