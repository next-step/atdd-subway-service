package nextstep.subway.exception;

import net.bytebuddy.implementation.bytecode.Throw;

public class StationNotExistException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "요청한 역이 존재하지 않습니다.";

  public StationNotExistException() {
    super(DEFAULT_MESSAGE);
  }

  public StationNotExistException(String message) {
    super(message);
  }

  public StationNotExistException(Throwable e) {
    super(DEFAULT_MESSAGE, e);
  }
}
