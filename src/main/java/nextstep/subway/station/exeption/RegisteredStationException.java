package nextstep.subway.station.exeption;

public class RegisteredStationException extends RuntimeException {

    public static final String MESSAGE = "이미 등록 된 역입니다.";

    public RegisteredStationException() {
        super(MESSAGE);
    }
}
