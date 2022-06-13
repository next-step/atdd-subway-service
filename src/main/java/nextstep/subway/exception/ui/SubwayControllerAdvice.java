package nextstep.subway.exception.ui;

import nextstep.subway.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.toString(), exception.getMessage()));
    }
}
