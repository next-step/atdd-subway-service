package nextstep.subway.common.Excetion.advice;

import nextstep.subway.common.Excetion.LineNotFoundException;
import nextstep.subway.common.Excetion.StationNotFoundException;
import nextstep.subway.common.Excetion.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.DuplicateFormatFlagsException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(DuplicateFormatFlagsException.class)
    public ResponseEntity handlerIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({LineNotFoundException.class, StationNotFoundException.class})
    public ResponseEntity handlerIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
    }
}
