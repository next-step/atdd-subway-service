package nextstep.subway.common.exception;

public class UnconnectedStationException extends RuntimeException {
    private static final String UNCONNECTED_STATION_EXCEPTION_MESSAGE = "출발역과 도착역이 연결되어 있지 않습니다.";
    private static final long serialVersionUID = 6L;

    public UnconnectedStationException() {
        super(UNCONNECTED_STATION_EXCEPTION_MESSAGE);
    }
}
