package nextstep.subway.station.application.exception;

public class StationNotFoundException extends RuntimeException {
    public static final StationNotFoundException NOT_FOUND_STATION = new StationNotFoundException("지하철 역을 찾을 수 없습니다.");

    public StationNotFoundException(String message) {
        super(message);
    }
}
