package nextstep.exception;

public class StationNotConnectedException extends BusinessException {

    private static final String ERROR_MESSAGE = "출발역과 도착역이 연결되지 않았습니다.";

    public StationNotConnectedException() {
        super(ERROR_MESSAGE);
    }
}
