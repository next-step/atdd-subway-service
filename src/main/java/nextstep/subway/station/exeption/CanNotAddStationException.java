package nextstep.subway.station.exeption;

public class CanNotAddStationException extends RuntimeException {

    public static final String MESSAGE = "등록할 수 없는 역입니다.";

    public CanNotAddStationException() {
        super(MESSAGE);
    }
}
