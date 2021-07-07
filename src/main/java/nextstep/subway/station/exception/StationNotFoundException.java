package nextstep.subway.station.exception;

public class StationNotFoundException extends RuntimeException{
    public StationNotFoundException() {
        super("역을 찾을 수 없습니다.");
    }
}
