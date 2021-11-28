package nextstep.subway.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.advice.dto.ExceptionResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
}
