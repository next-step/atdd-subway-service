package nextstep.subway.path.exception;

public class StationNotConnectException extends RuntimeException{

    private static final String STATION_NOT_CONNECTED = "station not connected.";

    public StationNotConnectException() {
        super(STATION_NOT_CONNECTED);
    }
}
