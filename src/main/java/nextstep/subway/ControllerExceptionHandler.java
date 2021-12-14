package nextstep.subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorResponse> serviceException(ServiceException exception) {
    return ResponseEntity.status(exception.status()).body(ErrorResponse.of(exception.getMessage()));
  }
}
