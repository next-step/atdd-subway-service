package nextstep.subway.station.exception;

public class CannotFoundStationException extends RuntimeException {

    public CannotFoundStationException() {
        super("지하철 역을 찾을 수 없습니다.");
    }

}
