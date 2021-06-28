package nextstep.subway.exception;

public class InvalidFeeAmountException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "요금이 음수가 될 수 없습니다.";

  public InvalidFeeAmountException() {super(DEFAULT_MESSAGE);}

  public InvalidFeeAmountException(String message) {
    super(message);
  }
}
