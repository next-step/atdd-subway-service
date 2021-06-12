package nextstep.subway.exception;

public class InvalidDistanceException extends RuntimeException {

  public InvalidDistanceException() {
    super();
  }

  public InvalidDistanceException(String message) {
    super(message);
  }

  public InvalidDistanceException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidDistanceException(Throwable cause) {
    super(cause);
  }
}
