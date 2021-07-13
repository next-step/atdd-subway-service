package nextstep.subway.station.exeption;

public class NotFoundStationException extends RuntimeException {

    public static final String MESSAGE = "역을 조회 할 수 없습니다.";

    public NotFoundStationException() {
        super(MESSAGE);
    }
}
