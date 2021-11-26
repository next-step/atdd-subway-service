package nextstep.subway.configuration;

import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({DuplicateDataException.class, InvalidDataException.class})
    public ResponseEntity<String> handleDuplicateAndInvalidDataException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
            e.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage))
        );
    }
}
