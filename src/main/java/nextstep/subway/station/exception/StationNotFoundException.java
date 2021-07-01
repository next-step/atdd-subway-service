package nextstep.subway.station.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("조회된 역이 없습니다.");
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
