package nextstep.subway.common.Excetion.advice;

import io.jsonwebtoken.JwtException;
import nextstep.subway.common.Excetion.*;
import nextstep.subway.common.Excetion.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.DuplicateFormatFlagsException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DuplicateFormatFlagsException.class)
    public ResponseEntity handlerIllegalArgsException(DuplicateFormatFlagsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({LineNotFoundException.class, StationNotFoundException.class})
    public ResponseEntity handlerIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(NotConnectStationException.class)
    public ResponseEntity handlerIllegalArgsException(NotConnectStationException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handlerIllegalArgsException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(UnableTokenException.class)
    public ResponseEntity handlerIllegalArgsException(UnableTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getMessage()));
    }
}
