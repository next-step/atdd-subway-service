package nextstep.subway.error.handler;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.error.ErrorResponse;
import nextstep.subway.error.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> businessException(BusinessException exception) {
    return ResponseEntity.status(exception.status()).body(ErrorResponse.of(exception.getMessage()));
  }

  @ExceptionHandler(AuthorizationException.class)
  public ResponseEntity<ErrorResponse> authorizationException(AuthorizationException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(exception.getMessage()));
  }
}
