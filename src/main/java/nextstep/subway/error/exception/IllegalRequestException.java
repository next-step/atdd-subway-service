package nextstep.subway.error.exception;

import org.springframework.http.HttpStatus;

public class IllegalRequestException extends BusinessException {
  public IllegalRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
