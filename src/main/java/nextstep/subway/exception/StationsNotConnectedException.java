package nextstep.subway.exception;

public class StationsNotConnectedException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "요청한 역 사이에 연결 된 구간이 없습니다.";

  public StationsNotConnectedException() {super(DEFAULT_MESSAGE);}

  public StationsNotConnectedException(String message) {
    super(message);
  }
}
