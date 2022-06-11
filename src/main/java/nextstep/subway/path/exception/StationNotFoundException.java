package nextstep.subway.path.exception;

public class StationNotFoundException extends RuntimeException{

    private static final String STATION_NOT_FOUND = "station not found.";

    public StationNotFoundException() {
        super(STATION_NOT_FOUND);
    }
}
